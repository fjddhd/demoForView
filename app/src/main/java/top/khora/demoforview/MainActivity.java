package top.khora.demoforview;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlowLayout mFlowLayout = (FlowLayout) findViewById(R.id.mFlowLayout);
        List<String> list = new ArrayList<>();
        list.add("java");
        list.add("javaEE");
        list.add("javaME");
        list.add("c");
        list.add("php");
        list.add("ios");
        list.add("c++");
        list.add("c#");
        list.add("Android");
        list.add("java");
        list.add("javaEE");
        list.add("javaME");
        list.add("c");
        list.add("php");
        list.add("ios");
        list.add("c++");
        list.add("c#");
        list.add("Android");
        list.add("java");
        list.add("javaEE");
        list.add("javaME");
        list.add("c");
        list.add("php");
        list.add("ios");
        list.add("c++");
        list.add("c#");
        list.add("Android");
        mFlowLayout.setAlignByCenter(FlowLayout.AlienState.CENTER);
        mFlowLayout.setAdapter(list, R.layout.item, new FlowLayout.ItemView<String>() {
            @Override
            void getCover(String item, FlowLayout.ViewHolder holder, View inflate, int position) {
                holder.setText(R.id.tv_label_name,item);
            }
        });
    }
}
