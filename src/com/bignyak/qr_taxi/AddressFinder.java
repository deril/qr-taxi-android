package com.bignyak.qr_taxi;

import android.location.Location;
import android.net.http.AndroidHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AddressFinder {
    public static void findAddress(final GpsActivity activity, final Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AndroidHttpClient client = AndroidHttpClient.newInstance("worldcamp2013");
                    HttpGet method = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + "," + location.getLongitude() + "&sensor=true");
                    String response = EntityUtils.toString(client.execute(method).getEntity());
                    if (!response.equals("")) {
                        JSONObject googleJSON = new JSONObject(response);
                        String address = googleJSON.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                        activity.updateAddress(address);
                    } else {
                        activity.updateAddressFailed("Request failed");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    activity.updateAddressFailed("Exception during request: " + e.getClass());
                }
            }
        }).start();
    }
}
