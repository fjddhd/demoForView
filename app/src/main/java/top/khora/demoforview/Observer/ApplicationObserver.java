package top.khora.demoforview.Observer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class ApplicationObserver implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)//只调用一次
    public void onCreate() {
        Log.d("ApplicationObserver","LIFECYCLE_onCreate");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.d("ApplicationObserver","LIFECYCLE_onStart");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.d("ApplicationObserver","LIFECYCLE_onResume");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.d("ApplicationObserver","LIFECYCLE_onPause");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Log.d("ApplicationObserver","LIFECYCLE_onStop");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)//永远不会调用
    public void onDestroy() {
        Log.d("ApplicationObserver","LIFECYCLE_onDestroy");
    }
}
