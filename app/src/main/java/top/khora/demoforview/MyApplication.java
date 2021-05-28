package top.khora.demoforview;

import android.app.Application;

import androidx.lifecycle.ProcessLifecycleOwner;

import dagger.hilt.android.HiltAndroidApp;
import leakcanary.LeakCanary;
import top.khora.demoforview.Observer.ApplicationObserver;

/**
 * ProcessLifecycleOwner用于给Application添加监听
 * */
@HiltAndroidApp//会触发 Hilt 的代码生成操作，生成的代码包括应用的一个基类，该基类充当应用级依赖项容器。
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationObserver());
    }
}
