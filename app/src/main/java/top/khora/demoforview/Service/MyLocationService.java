package top.khora.demoforview.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import javax.inject.Inject;

import top.khora.demoforview.Observer.MyLocationObserver;

public class MyLocationService extends LifecycleService {
    public static final String TAG="MyLocationService";

    public MyLocationService() {
        Log.d(TAG,"MyLocationService构造");
        MyLocationObserver myLocationObserver = new MyLocationObserver(this);
        getLifecycle().addObserver(myLocationObserver);
    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        Log.d(TAG,"onBind");
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }
}
