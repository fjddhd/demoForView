package top.khora.demoforview.Views;

import android.app.IntentService;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.core.view.VelocityTrackerCompat;

import java.util.ArrayList;
import java.util.List;

import top.khora.demoforview.R;

/**
 * 1 主体
 * */
public class MyRecyclerView extends ViewGroup{
    public static final String TAG="MyRecyclerView-";
    private boolean needRelayout;
    private List<View> viewList;//当前显示的view
    private Adapter mAdapter;
    private int width;
    private int height;//该RV高度
    private Recycler mRecycler;

    //Item
    private int rowCount;
    private int touchSlop;
    private int currentY;

    private int[] heights;//所有view高度

    //滑动相关fields
    private int scrollY;//第一个可见元素与屏幕上方距离
    //滑到第几行
    private int firstRow;//第一个可见元素在全部view中的index

    //惯性滑动
//    private VelocityTracker mVelocityTracker;
    private int scrollDist;
    private float scrollCycleTime;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        needRelayout=true;
        viewList=new ArrayList<>();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        //touch识别成滚动的最小距离，px为单位
    }
    public void setAdapter(Adapter adapter){
        needRelayout=true;
        if (adapter!=null){
            mAdapter=adapter;
            rowCount=adapter.getCount();
            heights=new int[rowCount];
            mRecycler=new Recycler(adapter.getViewTypeCount());
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (needRelayout || changed){
            needRelayout=false;
            viewList.clear();
            removeAllViews();//耗时
            if (mAdapter!=null){
//                rowCount=mAdapter.getCount();
                //每行高度
                for (int i = 0; i < rowCount; i++) {//测量item高度
                    heights[i]+=mAdapter.getHeight(i);
                }

                width=r-l;
                height=b-t;
                int top=0,bottom;
                for (int i = 0; i < rowCount && top<height; i++) {
                    Log.i("TEST_VIEW","rowCount:"+rowCount+",height:"+height+",top:"+top);
                    bottom=top+heights[i];

                    //布局实例化
                    View view=makeAndSetup(i,0,top,width,bottom);
                    viewList.add(view);
                    //如何摆放

                    //摆放多少
                    top=bottom;
                }
            }

        }
    }

    /**调用频率取决于invalidate(),因此也不应该在该方法内创建对象，否则容易内存抖动
    *解决方法:
     * 1. 全局变量
     * 2. 享元设计模式
     * 3. 本RV的回收池Recycler容器复用
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Paint paint = new Paint();
//        canvas.drawOval(new RectF(),paint);
    }

    private View makeAndSetup(int row, int left, int top, int right, int bottom) {
        View view=obtain(row,right-left,bottom-top);
        view.layout(left,top,right,bottom);//耗时
        return view;
    }

    private View obtain(int row, int width, int height) {//无宽高的view
        //先找回收池
        int itemViewType = mAdapter.getItemViewType(row);
        View viewFromRecycler = mRecycler.getRecyclerView(itemViewType);
        if (viewFromRecycler == null){
            viewFromRecycler=mAdapter.onCreateViewHolder(row,null,this);
            viewFromRecycler=mAdapter.onBindViewHolder(row,viewFromRecycler,this);
            if (viewFromRecycler==null){//防止用户onCreateViewHolder忘记返回view
                throw new RuntimeException("onCreateViewHolder  必须初始化");
            }
        }else {
            viewFromRecycler=mAdapter.onBindViewHolder(row,viewFromRecycler,this);
        }
//        viewFromRecycler.setTag(itemViewType);//把默认tag设置留给用户,本RV内部使用键值对重载方法
        if (viewFromRecycler==null){//防止用户onBindViewHolder忘记返回view
            throw new RuntimeException("onCreateViewHolder  必须初始化");
        }
        viewFromRecycler.setTag(R.id.tag_type_view,itemViewType);
        //测量
        viewFromRecycler.measure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));

        addView(viewFromRecycler,0);

        return viewFromRecycler;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        //type
//        Integer tag = (Integer) view.getTag();
        Integer type = (Integer) view.getTag(R.id.tag_type_view);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG,"111-dispatchTouchEvent:"+ev.toString());
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 事件处理相关方法:
     * - 滑动拦截
     * - 点击不拦截
     * -- 在一次循环中，只要有一次返回true（注意：如果外部拦截DOWN，连CANCEl也不传！）则 返回CANCEL给子控件
     * 该循环内之后的move up不继续传递(dispatch)给子控件，也不执行该RV的onIntercept，直接给该RV的onTouchEvent
     *
     * */

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept=false;
        int action = ev.getAction();
//        if (mVelocityTracker == null) {
//            mVelocityTracker = VelocityTracker.obtain();
//        }
//        boolean eventAddedToVelocityTracker = false;
        Log.i(TAG,"111-onInterceptTouchEvent:"+ev.toString());
        switch (action){
            case MotionEvent.ACTION_DOWN:
                currentY= (int) ev.getRawY();
                scrollDist=(int) ev.getRawY();
                scrollCycleTime = SystemClock.currentThreadTimeMillis();
//                intercept=!intercept;//测试外部拦截dwon事件
                break;
            case MotionEvent.ACTION_MOVE:
                int y2= (int) Math.abs(currentY-ev.getRawY());
                if (y2>touchSlop){//判断发生滑动,该RV拦截
                    intercept=true;//拦截后本循环之后事件只进入本RV的onTouchEvent方法，不传子控件
//                    for (int i = 0; i < getChildCount(); i++) {//子view可滑动的处理
//                        View childAt = getChildAt(i);
//                        if (childAt instanceof ScrollView){
//                            float rawX = ev.getRawX();
//                            float rawY = ev.getRawY();
//                            if (rawX>childAt.getLeft() && rawX<childAt.getRight()&&
//                            rawY>childAt.getTop() && rawY<childAt.getBottom()){
//                                intercept=false;
//                            }
//                        }
//                    }

                }
                break;
        }
        Log.i(TAG,"111-onInterceptTouchEvent-return:"+intercept);
        return intercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"111-onTouchEvent:"+event.toString());
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int Y2 = (int) event.getRawY();
                int diff=currentY-Y2;//旧点减新点，比如下滑是为负值（加上移下）,上滑是正值(加下移上)
                currentY=Y2;//必须要加，否则会以down时间为锚点，单次滑动加速
                scrollBy(0, diff);
                break;
            case MotionEvent.ACTION_UP:
                scrollDist= (int) event.getRawY()-scrollDist;
                scrollCycleTime =SystemClock.currentThreadTimeMillis()- scrollCycleTime;
                float scrollVelocity=scrollDist/(scrollCycleTime /500);
                Log.i(TAG,"scrollVelocity:"+scrollVelocity);
//                scrollBy(0,-(int)scrollVelocity);
                //-TODO 惯性滑动实现

                break;
        }
        return super.onTouchEvent(event);

    }

    //ScrollBy 是相对滚动；ScrollTo 是绝对滚动,
    //由 onTouchEvent调用，可能用户滑动快时需要在一次scrollBy中移除多个元素
    @Override
    public void scrollBy(int x, int diff) {
//        super.scrollBy(x, diff);//默认滑动的Canvas
//        scrollY+=diff;
//
//        repositionViews();

        scrollY+=diff;
        //修正
        Log.i(TAG,"srcollBy-scrollY:"+scrollY);
        scrollY=scrollBounds(scrollY,firstRow,heights,height);
        Log.i(TAG,"srcollBy-scrollBounds-scrollY:"+scrollY);
        if (scrollY>0){//上滑动
            while (heights[firstRow]<scrollY){//与RV上部距离大于第一可见元素高度，则移除第一可见元素
                if (!viewList.isEmpty()){
                    removeView(viewList.remove(0));
                    Log.i(TAG,"上滑-移除:"+firstRow);
                    /**
                     * 不可以移除一个马上添加一个
                     * 因为有可能下一个需要添加的元素是其他类型的，具有不同的高度
                     * */
//                    addView(obtain());
                }
                scrollY-=heights[firstRow];
                firstRow++;
            }
            while (getFilledHeight()<height){//添加View
                final int size=viewList.size();
                int dataIndex=firstRow+size;
                Log.i(TAG,"上滑-添加:"+dataIndex);
                View obtainedView = obtain(dataIndex, width, heights[dataIndex]);
                viewList.add(obtainedView);

            }
        }else {//下滑
            while (!viewList.isEmpty() &&
            getFilledHeight()-heights[firstRow+viewList.size()-1]<scrollY){
                Log.i(TAG,"下滑-移除:"+(firstRow+viewList.size()-1));
                removeView(viewList.remove(viewList.size()-1));
            }
            while (0>scrollY){
                firstRow--;
                Log.i(TAG,"下滑-移除:"+firstRow);
                View obtainedView = obtain(firstRow, width, heights[firstRow]);
                viewList.add(0,obtainedView);
                scrollY+=heights[firstRow+1];
            }
        }
        repositionViews();

    }
    private int scrollBounds(int scrollY, int firstRow, int[] sizes, int viewSize){
        //修正函数，处理最上最下的极限值情况
        if (scrollY==0){
            //no op
        }else if (scrollY<0){//修整下滑临界值
            scrollY=Math.max(scrollY,-sumArray(sizes,0,firstRow));
        }else {//上滑
            scrollY=Math.min(scrollY,Math.max(0,sumArray(sizes,firstRow,sizes.length)));
        }
        return scrollY;
    }
    private int getFilledHeight() {//获取显示的元素的的高度
        return sumArray(heights,firstRow,viewList.size())-scrollY;
    }
    private int sumArray(int[] array, int firstIndex, int count) {//计算可见元素总高度
        int sum=0;
        for (int i = firstIndex; i < count; i++) {
            sum+=array[i];
        }
        return sum;
    }

    private void repositionViews() {
        int l,t,r,b,i;
        t=-scrollY;
        i=0;
        for (View view : viewList) {
            b=t+heights[i++];
            view.layout(0,t,width,b);
            t=b;
        }
    }

    /**
     * 3 适配器
     * */
    public interface Adapter{
        View onCreateViewHolder(int position, View convertView, ViewGroup parent);
        View onBindViewHolder(int position, View convertView, ViewGroup parent);
        int getItemViewType(int row);
        int getViewTypeCount();
        int getCount();
        int getHeight(int index);
    }
}
