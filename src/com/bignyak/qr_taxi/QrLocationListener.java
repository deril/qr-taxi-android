package com.bignyak.qr_taxi;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class QrLocationListener implements LocationListener {
    GpsActivity activity;
    QrLocationListener(GpsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onLocationChanged(Location location) {
        activity.updateLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
