package top.khora.demoforview.Observer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class MyLocationObserver implements LifecycleObserver {
    public static final String TAG="MyLocationObserver";

    private final Context mContext;
    private LocationManager locationManager;
    private LocationListener mLocationListener;

    public MyLocationObserver(Context context) {
        mContext = context;
    }

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void startGetLocation() {
        Log.d(TAG,"startGetLocation");
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000,
                1,
                new LocationListener());
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void stopGetLocation(){
        Log.d(TAG,"stopGetLocation");
        locationManager.removeUpdates(mLocationListener);

    }
    static class LocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG,"onLocationChanged"+location.toString());
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
}
