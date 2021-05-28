package top.khora.demoforview.Fragment;

/**
 * Created by 佳佳 on 2015/12/31.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import top.khora.demoforview.R;

public class OneFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_one, container, false);//关联布局文件

        //设置在OneFragment中的点击效果
        Button bt_frg_one = (Button) rootView.findViewById(R.id.bt_frg_one);
        bt_frg_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "这是第一页按钮的点击效果", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}