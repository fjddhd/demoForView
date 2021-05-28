package top.khora.demoforview.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {
    public static final String TAG="MyTextView";
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"111-222-onTouchEvent:"+event.toString());
        return super.onTouchEvent(event);//其中包含了传递给click
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG,"111-222-dispatchTouchEvent:"+ev.toString());
        return super.dispatchTouchEvent(ev);
    }
}
