package top.khora.demoforview.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

@SuppressLint("AppCompatCustomView")
public class Chronometer extends android.widget.Chronometer implements LifecycleObserver {
    public static final String TAG="ChronometerFJD";
    private long elapsedRealtime;

    public Chronometer(Context context) {
        super(context);
    }

    public Chronometer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Chronometer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Chronometer(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void startMeter(){
        setBase(SystemClock.elapsedRealtime()-elapsedRealtime);
        start();
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void stopMeter(){
        elapsedRealtime = SystemClock.elapsedRealtime() - getBase();
        stop();
    }


}
