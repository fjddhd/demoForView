package top.khora.demoforview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    private TextView tv_header;
    private Button btnHeader;
    private Button btn_startVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlowLayout2 mFlowLayout = (FlowLayout2) findViewById(R.id.mFlowLayout);
        tv_header = findViewById(R.id.tv_main_header);
        btnHeader = findViewById(R.id.btn_main_head);
        btn_startVp = findViewById(R.id.btn_vp_activity);
        btn_startVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,VpFragActivity.class);
                startActivity(intent);
            }
        });
        btnHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitHeader();
            }
        });
        List<String> list = initialData(new String());
//        mFlowLayout.setAlignByCenter(FlowLayout.AlienState.CENTER);
        mFlowLayout.setAdapter(list, R.layout.item, new FlowLayout2.ItemView<String>() {
            @Override
            void getCover(String item, FlowLayout2.ViewHolder holder, View inflate, int position) {
                holder.setText(R.id.tv_label_name,item);
            }
        });
        ViewPager vp;
        RecyclerView rv=findViewById(R.id.rv_main);
        LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(mLinearLayoutManager);
        rv.setAdapter(new myAdapterImage(list,this));
    }
    public void retrofitHeader(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://borche.khora.top/")
                .build();
        //获取API
        CoffService service = retrofit.create(CoffService.class);
        Call<ResponseBody> call= service.getData("1");
        //异步
        call.enqueue(new Callback<ResponseBody>() {//CallBack方法运行在主线程！
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //处理请求数据
                ResponseBody body = response.body();
                try {
                    tv_header.setText(body.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
        //同步
//        try {
//            Response<ResponseBody> executedResponse = call.execute();
//            executedResponse.body().toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    //定义 网络API 地址
    public interface CoffService {
        @GET("admin/capture/querybyid/{id}")
        Call<ResponseBody> getData(@Path("id") String id);
    }

    public void hotFix(){
        /**
         * - 获取PathCL
         * - 反射得到DexPathList类型的对象pathList
         * - 反射修改pathList的dexElements
         * -- 把补丁包patch.dex转化为Element[] (patch)
         * -- 获得oathList的dexElements属性 (old)
         * -- path+dexElements合并，并反射复制给pathList的dexElements
         * */
        ClassLoader classLoader = MainActivity.class.getClassLoader();
        Class<? extends ClassLoader> aClass = classLoader.getClass();
        try {
            Field pathList = aClass.getDeclaredField("pathList");
            pathList.setAccessible(true);
            try {
                pathList.set(classLoader,pathList);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public <T extends Object> List<T> initialData(T t){
        List<T> list = new ArrayList<>();
        list.add((T)"java");
        list.add((T)"javaEE");
        list.add((T)"javaME");
        list.add((T)"c");
        list.add((T)"php");
        list.add((T)"ios");
        list.add((T)"c++");
        list.add((T)"c#");
        list.add((T)"Android");
        list.add((T)"java");
        list.add((T)"javaEE");
        list.add((T)"javaME");
        list.add((T)"c");
        list.add((T)"php");
        list.add((T)"ios");
        list.add((T)"c++");
        list.add((T)"c#");
        list.add((T)"Android");
        list.add((T)"java");
        list.add((T)"javaEE");
        list.add((T)"javaME");
        list.add((T)"c");
        list.add((T)"php");
        list.add((T)"ios");
        list.add((T)"c++");
        list.add((T)"c#");
        list.add((T)"Android");
        return list;
    }

}
