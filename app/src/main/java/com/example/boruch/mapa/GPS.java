package com.example.boruch.mapa;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import java.util.jar.Manifest;

/**
 * Created by Boruch on 2017-06-10.
 */

public class GPS extends Service implements LocationListener{

    private final Context context;
    boolean enabled=false;
    boolean networkEnabled=false;

    Location location;
    protected LocationManager locationManager;

    public GPS(Context context){
        this.context=context;
    }
    public Location getLocation(){
        try{
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // Stan dla GPS i sieci, odpowiednie pozwolenia w Manifeście
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED ){ // Sprawdza czy mamy odpowiednie pozwolenia
                if(enabled){ // Jeśli mamy dostęp do GPS
                    if(location==null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10,this); // Sprawdza lokalizację co 10 sek.
                        if(locationManager!=null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); // Zwraca ostatnią lokalizacje
                        }
                    }
                }
                if(location==null){
                    if(networkEnabled){ // Mamy dostęp do sieci
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,10,this); // Sprawdza lokalizację za pomocą sieci co 10 sek
                            if(locationManager!=null){
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }
                    }
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return location;
    }
    public void onLocationChanged(Location location){
    }
    public void onStatusChanged(String provider, int status, Bundle ex){

    }
    public IBinder onBind(Intent intent){
        return null;
    }
    public void onProviderDisabled(String prov){

    }
    public void onProviderEnabled(String prov){

    }
}
