package com.bignyak.qr_taxi;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class GpsActivity extends Activity {
    public TextView latView, lngView;
    public LocationManager lm;
    public LocationListener ll;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("QR-Taxi GPS");
        latView = (TextView) findViewById(R.id.latView);
        lngView = (TextView) findViewById(R.id.lngView);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new QrLocationListener(GpsActivity.this);
        setContentView(R.layout.main);
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
        latView.setText((int) location.getLatitude());
        lngView.setText((int) location.getLongitude());
    }
}
