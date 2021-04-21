package top.khora.demoforview;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BroadcastTestActivity extends AppCompatActivity {
    public static final String TAG = "MainThread";

    public static final String ACTION_SEND_MSG_BY_MYINTENTSERVICE = "top.khora.by.myIntentService";
    public static final String ACTION_FROM_INTENTSERVICE = "top.khora.intentService";

    private Button btn_send_intent_service;
    private TextView tv_msg, tv_msg_intent_service;

    private DownloadBroadcastReceiver downloadBroadcastReceiver;

    // 使用本地广播 LocalBroadcast 会更好些
    class DownloadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Skipped 5999 frames!  在这里直接执行耗时操作：会提示丢帧，或者出现 ANR 弹窗
            // SystemClock.sleep(100000);

            tv_msg.append("--onReceive: action = " + intent.getAction());
            tv_msg.append("\n");
            String data = intent.getStringExtra("data");

            // 3. 执行耗时操作
            if (ACTION_SEND_MSG_BY_MYINTENTSERVICE.equals(intent.getAction())) {
                tv_msg_intent_service.append("--onReceiver: process time-consuming by IntentService !");
                tv_msg_intent_service.append("\n");

                // 方式二：使用 IntentService 处理耗时操作，并且通过广播回传处理成功的回调
                sendMsgByIntentService(context, data);

            } else if (ACTION_FROM_INTENTSERVICE.equals(intent.getAction())) {

                // 方式二回调结果：Service通过广播发送数据，更新UI
                tv_msg_intent_service.append("\n");
                tv_msg_intent_service.append("--onReceiver: received data from IntentService !");
                tv_msg_intent_service.append("\n");
                tv_msg_intent_service.append("--data = " + intent.getStringExtra("data_from_intent_service"));
                tv_msg_intent_service.append("\n");

            }
        }
    }

    /**
     * 策略二：使用 IntentService处理
     * @param data
     */
    private void sendMsgByIntentService(Context context, String data) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction("com.xss.startIntentService");
        intent.putExtra("data", data);
        context.startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. 注册广播
        registerBroadcast();

        initView();
    }

    private void registerBroadcast() {
        downloadBroadcastReceiver = new DownloadBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("");
        intentFilter.addAction(ACTION_SEND_MSG_BY_MYINTENTSERVICE);
        intentFilter.addAction(ACTION_FROM_INTENTSERVICE);
        registerReceiver(downloadBroadcastReceiver, intentFilter);
    }

    private void initView() {
    }

    private void sendBroadcast(String action) {
        // 2. 发送广播
        Intent intent = new Intent(action);
        intent.putExtra("data", "hello, This is a msg from broadcast to send to you !");
        sendBroadcast(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadBroadcastReceiver);
    }
    class MyIntentService extends IntentService{

        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public MyIntentService(String name) {
            super(name);
        }

        // 该方法是在 HandlerThread异步消息处理线程的消息队列中取出消息进行处理，当所有的msg处理完，messageQueue为空，IntentService 就会调用 stopSelf 方法停止
        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            Log.d(TAG, "IntentService is handling intent msg");

            if ("com.xss.startIntentService".equals(intent.getAction())) {
                String msg = intent.getStringExtra("data");

                Log.d(TAG, Thread.currentThread().getName());
                Log.d(TAG, "Msg received from main handler thread: broadcast send = " + msg);
                // 处理耗时请求
                SystemClock.sleep(5000);
                Log.d(TAG, "Is Broadcast ANR ?");

                // 子线程处理消息后，给主线程发消息，说"我执行完了"
                Message mainMsg = new Message();
                mainMsg.what = 0x22;
                mainMsg.obj = "Msg from child handler thread: I have done !";

                // 使用广播将处理耗时操作的数据发送给主线程
                Intent intent0 = new Intent(BroadcastTestActivity.ACTION_FROM_INTENTSERVICE);
                intent0.putExtra("data_from_intent_service", "Msg from MyIntentService: I have done !");
                sendBroadcast(intent0);

                // 如果消息队列为空，就会直接结束Service，接着执行onDestroyed方法
            }
        }
    }
}