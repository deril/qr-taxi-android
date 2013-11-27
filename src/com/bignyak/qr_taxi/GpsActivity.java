package com.bignyak.qr_taxi;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class GpsActivity extends Activity {
    public TextView latView, lngView, addressView;
    public LocationManager lm;
    public LocationListener ll;
    public long lastRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("QR-Taxi GPS");
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new QrLocationListener(this);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        latView = (TextView) findViewById(R.id.latView);
        lngView = (TextView) findViewById(R.id.lngView);
        addressView = (TextView) findViewById(R.id.addressView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, ll);
    }

    @Override
    protected void onPause() {
        super.onPause();
        new Thread(new Runnable() {
            @Override
            public void run() {
                lm.removeUpdates(ll);
            }
        }).start();
    }

    public void updateLocation(Location location) {
        latView.setText(Double.toString(location.getLatitude()));
        lngView.setText(Double.toString(location.getLongitude()));
        if (lastRequest == 0 || (new Date().getTime() - lastRequest) > 60) {
            lastRequest = new Date().getTime();
            AddressFinder.findAddress(this, location);
        }
    }

    public void updateAddress(final String address) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addressView.setText(address);
            }
        });
    }

    public void updateAddressFailed(final String message) {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, message, duration).show();
            }
        });
    }
}
