package top.khora.demoforview;

import android.app.Application;

import androidx.lifecycle.ProcessLifecycleOwner;

import top.khora.demoforview.Observer.ApplicationObserver;

/**
 * ProcessLifecycleOwner用于给Application添加监听
 * */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationObserver());
    }
}
