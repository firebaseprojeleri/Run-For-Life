package com.example.fatih.runforlife;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

/**
 * Created by Fatih on 20.04.2017.
 */

public class GPSTracker extends Service implements LocationListener {

    boolean isGPSEnabled=false;
    boolean isNetworkEnabled=false;
    boolean canGetLocation=false;

    Location location;
    protected LocationManager locationManager;

    private  final Context context;
    public GPSTracker(Context context)
    {
        this.context=context;
    }

    public  Location getLocation()
    {
        try
        {
            locationManager=(LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled=locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            isNetworkEnabled=locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)


            {
                if(isGPSEnabled)
                {
                    if(location==null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10,this);
                        if(locationManager!=null)
                        {
                            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        }
                    }
                }
                if(location==null)
                {
                    if(isNetworkEnabled)
                    {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,10,this);
                        if(locationManager!=null)
                        {
                            location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }

                    }
                }
            }
        }catch (Exception ex) {

        }
        return location;

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

