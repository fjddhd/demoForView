package top.khora.demoforview.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

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
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
