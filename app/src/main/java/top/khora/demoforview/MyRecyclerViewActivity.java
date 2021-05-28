package top.khora.demoforview;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import top.khora.demoforview.Views.MyRecyclerView;

public class MyRecyclerViewActivity extends AppCompatActivity {

    private MyRecyclerView myRV;
    private final String TAG="MyRecyclerViewActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_recycler_layout);
        myRV = findViewById(R.id.my_recyclerview);
        myRV.setAdapter(new myAdapter(this));
    }

    class myAdapter implements MyRecyclerView.Adapter{
        LayoutInflater inflater;
        private int height;
        private myAdapter.myClickListener myClickListener;

        public myAdapter(Context context){
            Resources resources = context.getResources();
            height=resources.getDimensionPixelSize(R.dimen.item_height);//通过dimen的dp值获取px值
            Log.i(TAG,"height--px:"+height);
            inflater=LayoutInflater.from(context);
            myClickListener = new myClickListener();
        }

        @Override
        public View onCreateViewHolder(int position, View convertView, ViewGroup parent) {
            if (getItemViewType(position)==1){
                if (convertView==null){
                    convertView = inflater.inflate(R.layout.item_image_test,parent,false);
                }
            }else {
                if (convertView==null){
                    convertView = inflater.inflate(R.layout.item_image_test2,parent,false);
                }
            }
            Log.i(TAG,"onCreateViewHolder:hashcode:"+convertView.hashCode());
            return convertView;
        }

        @Override
        public View onBindViewHolder(int position, View convertView, ViewGroup parent) {
            Log.i(TAG,"onBindViewHolder:hashcode:"+convertView.hashCode());
            TextView tv;
            if (getItemViewType(position)==1){
                tv = convertView.findViewById(R.id.tv_label_name_test);
                tv.setText("第"+position+"行,type:"+getItemViewType(position));
            }else {
                tv = convertView.findViewById(R.id.tv_label_name_test2);
                tv.setText("第"+position+"行,type:"+getItemViewType(position));
            }
            tv.setOnClickListener(myClickListener);
            return convertView;
        }

        @Override
        public int getItemViewType(int row) {
            if (row%2==0){
                return 0;
            }
            return 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getCount() {
            return 5000000;
        }

        @Override
        public int getHeight(int index) {
            return height;
        }

        class myClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                TextView tv=(TextView)v;
                Toast.makeText(MyRecyclerViewActivity.this,tv.getText(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
