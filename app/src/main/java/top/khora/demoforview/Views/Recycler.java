package top.khora.demoforview.Views;

import android.view.View;

import java.util.Stack;

/**
 * 2 回收池
 * */
public class Recycler {

    //集合--》栈的数组
    private Stack<View>[] views;
    public Recycler(int viewTypeCount) {
        views=new Stack[viewTypeCount];
        for (int i = 0; i < viewTypeCount; i++) {
            views[i]=new Stack<View>();
        }
    }

    //取对应类型view
    public View getRecyclerView(int type){
        try{
            return views[type].pop();
        } catch (Exception e){
            return null;
        }
    }
    //存对类型类view
    public void addRecycledView(View view, int type){
        views[type].push(view);
    }
}
