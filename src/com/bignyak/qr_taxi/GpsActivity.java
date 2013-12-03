package com.bignyak.qr_taxi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class GpsActivity extends Activity {
    public EditText addressEdit;
    public LocationManager lm;
    public LocationListener ll;
    public long lastRequest = 0;
    public final Context context = this;

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
        addressEdit = (EditText) findViewById(R.id.editAddress);
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
        if (lastRequest == 0 || (new Date().getTime() - lastRequest) > 60) {
            lastRequest = new Date().getTime();
            AddressFinder.findAddress(this, location);
        }
    }

    public void updateAddress(final String address) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addressEdit.setText(address);
            }
        });
    }

    public void updateAddressFailed(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, message, duration).show();
            }
        });
    }

    public void getAddressManual(View view) {
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateLocation(location);
    }

    public void alertSendParams(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Are you shure?");
        alertDialogBuilder
                .setMessage("Do you want to call taxi with this parameters?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: add here call function to send request to server
                        GpsActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
