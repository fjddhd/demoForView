package top.khora.demoforview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout2 extends ViewGroup {
    private Context mContext;
    private int mItemMarginVertical=dipToPx(getContext(),8);//每个item的垂直间距
    private int mItemMarginHorizon=dipToPx(getContext(),16);//每个item的垂直间距
    public static int dipToPx(Context ctx, float dip) {
        return (int) TypedValue.applyDimension(1, dip, ctx.getResources().getDisplayMetrics());
    }
    private List<List<View>> allLines;//分行记录view
    List<Integer> linesHeight=new ArrayList<>();//记录行高
    /**
     * 四个构造函数
     * 前三个通常都要实现
     * */
    public FlowLayout2(Context context) {
        super(context);
        mContext=context;
    }
    //XML解析时,反射
    public FlowLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }
    //自定义属性，自定义主题
    public FlowLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
    }
    //
    public FlowLayout2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext=context;
    }

    /**
     * 记录初始化函数
     * */
    private void initial(){
        allLines=new ArrayList<>();
        linesHeight=new ArrayList<>();
    }
    /**
     * 三大流程函数
     * */
    /**
     * 确定子View大小
     * 通过子View大小确定自己大小
     * 对于流式布局：宽为所有行中最宽的行的宽度
     *              高维所有行中最高view的高度之和
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//父View传入的推荐值
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initial();//一定要在这里初始化，不可以在构造函数初始化

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        //获取从父View输入宽高的Size
        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);

        //行相关属性
        List<View> lineViews=new ArrayList<>();//一行中所有的view
        int lineWidthUsed=0;//当前行已用尺寸
        int lineHeight=0;//一行的高度

        int needHeight=0,needWidth=0;//该View需要的宽高


        //度量子View大小
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            LayoutParams childLP = child.getLayoutParams();//相当于拿到子View的xml中定义的各种属性
            int childWidthMS=getChildMeasureSpec(widthMeasureSpec,
                    paddingLeft+paddingRight,childLP.width);
            int childHeightMS=getChildMeasureSpec(heightMeasureSpec,
                    paddingLeft+paddingRight,childLP.height);
            child.measure(childWidthMS,childHeightMS);

            //子View度量后的宽高
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();

            //换行
            if (lineWidthUsed+measuredWidth>selfWidth){
                //更新记录
                allLines.add(lineViews);
                linesHeight.add(lineHeight);
                //更新该View需要宽高
                needHeight+=(lineHeight+mItemMarginHorizon);
                needWidth=Math.max(needWidth,lineWidthUsed+mItemMarginHorizon);

                //重置行相关属性
                lineViews=new ArrayList<>();
                lineWidthUsed=0;
                lineHeight=0;
            }
            //行内追加item
            lineViews.add(child);
            lineWidthUsed+=(measuredWidth+mItemMarginHorizon);
            lineHeight=Math.max(lineHeight,measuredHeight);

            if (i==childCount-1){//最后一个节点
                //更新记录
                allLines.add(lineViews);
                linesHeight.add(lineHeight);
                //更新该View需要宽高
                needHeight+=(lineHeight+mItemMarginHorizon);
                needWidth=Math.max(needWidth,lineWidthUsed+mItemMarginHorizon);
                //最后一次无所谓重置不重置了
            }
        }
        //确定自己大小
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(needWidth,widthMode)
                ,MeasureSpec.makeMeasureSpec(needHeight,heightMode));

    }

    /**
     *确定子View坐标
     * */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int count=getChildCount();
//        for (int i = 0; i < count; i++) {
//            View child = getChildAt(i);
//
//        }
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();


        int lineSize = allLines.size();
        int top=paddingTop;
        for (int i = 0; i < lineSize; i++) {
            List<View> views = allLines.get(i);
            int left=paddingLeft;
            for (int j = 0; j < views.size(); j++) {
                View view = views.get(j);
                int right=left+view.getMeasuredWidth(),bottom=top+view.getMeasuredHeight();
                view.layout(left,top,right,bottom);
                left+=view.getMeasuredWidth()+mItemMarginHorizon;
            }
            top+=linesHeight.get(i);
        }
    }

    /**
     *
     * */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setAdapter(List<?> list, int res, FlowLayout2.ItemView mItemView) {//适配器模式
        if (list == null) {
            return;
        }
        removeAllViews();
        int layoutPadding = dipToPx(getContext(), 8);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Object item = list.get(i);
            View inflate = LayoutInflater.from(getContext()).inflate(res, null);//item背景
            //使用时重写getCover方法对viewHolder内容执行相关操作方法
            mItemView.getCover(item,
                    new FlowLayout2.ViewHolder(inflate),
                    inflate,
                    i);
            addView(inflate);
        }
    }

    public abstract static class ItemView<T> {
        abstract void getCover(T item, FlowLayout2.ViewHolder holder, View inflate, int position);
    }

    //所有的子控件
    private SparseArray<View> mViews;

    class ViewHolder {
        View mConvertView;

        public ViewHolder(View mConvertView) {
            this.mConvertView = mConvertView;
            //保证所有viewHolder构造之后（adapter把list中所有要添加的view内容加进来后），才让mView保持初始化状态
            mViews = new SparseArray<>();
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            try {
                return (T) view;
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void setText(int viewId, String text) {
            TextView view = getView(viewId);
            view.setText(text);
        }
    }
}
