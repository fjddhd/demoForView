package top.khora.demoforview;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
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
    private Button btn_lifecycle;
    private Button btn_lifecycleService;
    private Button btn_myRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlowLayout2 mFlowLayout = (FlowLayout2) findViewById(R.id.mFlowLayout);
        tv_header = findViewById(R.id.tv_main_header);
        btnHeader = findViewById(R.id.btn_main_head);
        btn_startVp = findViewById(R.id.btn_vp_activity);
        btn_lifecycle = findViewById(R.id.btn_lifecycle);
        btn_lifecycleService = findViewById(R.id.btn_lifecycleService);
        btn_myRecyclerView = findViewById(R.id.btn_myrecyclerview);
        btn_myRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MyRecyclerViewActivity.class));
            }
        });
        btn_lifecycleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LcServiceActivity.class));
            }
        });
        btn_lifecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LifeCycleActivity.class));
            }
        });
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

        RecyclerView rv=findViewById(R.id.rv_main);
        LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(mLinearLayoutManager);
        rv.setAdapter(new myAdapterImage(list,this));

        LinearLayout linearLayout_btns = findViewById(R.id.ll_btns);
        linearLayout_btns.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.w("fjddhd","touch: "+event.toString());
                return false;
            }
        });
        linearLayout_btns.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.w("fjddhd","onLongClick");
                return false;
            }
        });
        linearLayout_btns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("fjddhd","onClick");
            }
        });
        //????????????view
        TextView textView_NewAdded = new TextView(this);
        textView_NewAdded.setText("??????View");
        ViewGroup.LayoutParams layoutParams=new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView_NewAdded.setLayoutParams(layoutParams);
        textView_NewAdded.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout_btns.addView(textView_NewAdded);

    }

    public void retrofitHeader(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://borche.khora.top/")
                .build();
        //??????API
        CoffService service = retrofit.create(CoffService.class);
        Call<ResponseBody> call= service.getData("1");
        //??????
        call.enqueue(new Callback<ResponseBody>() {//CallBack???????????????????????????
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //??????????????????
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
        //??????
//        try {
//            Response<ResponseBody> executedResponse = call.execute();
//            executedResponse.body().toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    //?????? ??????API ??????
    public interface CoffService {
        @GET("admin/capture/querybyid/{id}")
        Call<ResponseBody> getData(@Path("id") String id);
    }

    public void hotFix(){
        /**
         * - ??????PathCL
         * - ????????????DexPathList???????????????pathList
         * - ????????????pathList???dexElements
         * -- ????????????patch.dex?????????Element[] (patch)
         * -- ??????oathList???dexElements?????? (old)
         * -- path+dexElements???????????????????????????pathList???dexElements
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
