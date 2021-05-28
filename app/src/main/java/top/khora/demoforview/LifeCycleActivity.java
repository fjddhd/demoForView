package top.khora.demoforview;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ThreadPoolExecutor;

import top.khora.demoforview.CustomView.Chronometer;

public class LifeCycleActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long elapsedRealtime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lifecycle_layout);
        chronometer = findViewById(R.id.life_tv_time);
        getLifecycle().addObserver(chronometer);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        chronometer.setBase(SystemClock.elapsedRealtime()-elapsedRealtime);
//        chronometer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        elapsedRealtime = SystemClock.elapsedRealtime() - chronometer.getBase();
//        chronometer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
