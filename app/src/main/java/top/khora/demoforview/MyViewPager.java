package top.khora.demoforview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {

    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height=0;
        //遍历所有child高度
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            //一定要用layoutParams
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            /**
             *  @param spec The requirements for this view
             *  @param padding The padding of this view for the current dimension and
             *         margins, if applicable
             *  @param childDimension How big the child wants to be in the current
             *         dimension
             * */
            childAt.measure(widthMeasureSpec,
                    getChildMeasureSpec(heightMeasureSpec,0,layoutParams.height));

            int measuredHeight = childAt.getMeasuredHeight();
            height=Math.max(height,measuredHeight);
        }
        heightMeasureSpec=MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
