package com.example.millie.smartshoppingmall_yly;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.millie.smartshoppingmall_yly.message.WebSocketConnection;
import com.example.millie.smartshoppingmall_yly.message.WebSocketConnectionHandler;
import com.example.millie.smartshoppingmall_yly.message.WebSocketException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int FLAG = 0x01;
    public String serverIP = "192.168.0.106:8080";
    public int n;
    // public String url = "http://" + serverIP +"/goodspic/"+n+ ".jpg";
    public String url;
    public String pathroot = Environment.getExternalStorageDirectory() + File.separator;
    //public String fileName = n+ ".jpg";
    public String fileName;
    public String filePath = pathroot + fileName;
    public String client;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {

            // TODO
            // 在这里进行 http request.网络请求相关操作
            //   for(n=1;n<4;n++) {
            File file = null;
            InputStream inputstream = null;
            OutputStream output1 = null;   //创建一个写入字节流对象

            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhh=" + n);
            try {
                // 创建一个URL对象
                URL urls = new URL(url);
                // 创建一个Http连接
                HttpURLConnection urlConn = (HttpURLConnection) urls.openConnection();
                // 使用IO流读取数据
                inputstream = urlConn.getInputStream();
                File file2 = new File(filePath);
                if (inputstream != null) {
                    if (file2.exists())
                        file2.delete();
                    file = new File(filePath);
                    file.createNewFile();
                    output1 = new FileOutputStream(file);
                    byte buffer1[] = new byte[4 * 1024];//每次读取4K
                    int num = 0;      //需要根据读取的字节大小写入文件
                    while ((num = (inputstream.read(buffer1))) != -1) {
                        output1.write(buffer1, 0, num);
                        System.out.println("fileout" + buffer1);
                    }
                    output1.flush();  //清空缓存
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    output1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "请求结果");
            msg.setData(data);
            handler.sendMessage(msg);


        }
    };
    private String ad = null;
    private Handler mEventHandler;
    private AutoTextView mAutoTextView;
    private int mLoopCount = 0;
    private ArrayList<String> mStringArray = new java.util.ArrayList<String>();
    private EditText mEdit;//搜索内容
    private Button mButton;//搜索按钮
    private ImageView marker;//marker
    private Button button_fruit;
    private Button button_meat;
    private Button button_daily;
    private Button button_other;
    private ImageView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        serverIP = bundle.getString("ip");
        client=bundle.getString("client");



//        //服务端图片下载
//        donwloadpic(serverIP);

        //地图指向广告
        setButtonActivity();


//公告栏


//        final WebSocketConnection wsc = new WebSocketConnection();
//        try {
//            wsc.connect("ws://" + serverIP + "/websocket01/chat.ws?username=ws-android-client2", new WebSocketConnectionHandler() {
//                public void onBinaryMessage(byte[] payload) {
//                    System.out.println("onBinaryMessage size=" + payload.length);
//                }
//
//                @Override
//                public void onClose(int code, String reason) {
//                    System.out.println("onClose reason=" + reason);
//                }
//
//                @Override
//                public void onOpen() {
//                    System.out.println("onOpen");
//                    wsc.sendTextMessage("欢迎光临!");
//					wsc.disconnect();
//                }
//
//                @Override
//                public void onRawTextMessage(byte[] payload) {
//                    System.out.println("onRawTextMessage size=" + payload.length);
//                    //	tv.setText(payload);
//                }
//
//                @Override
//                public void onTextMessage(String payload) {
//                    //tv.setText(payload);
//                    ad = payload;
//                    setBulletin();
//                    mAutoTextView.setText(payload);
//
//                }
//
//            });
//        } catch (WebSocketException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        //载入公告栏
       setBulletin();


        //载入地图
        map = (ImageView) findViewById(R.id.map_image);
        map.setImageResource(R.mipmap.map);

        setSearch();
        marker = (ImageView) findViewById(R.id.map_marker);
        marker.setImageResource(R.mipmap.marker);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);//浮动按钮
        fab.setImageResource(R.mipmap.ad);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sheet = 0;
                map.setImageResource(R.mipmap.map);
                Intent myntent = new Intent(MainActivity.this, AdActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("pathroot", pathroot);
                bundle.putInt("sheet", sheet);
                myntent.putExtras(bundle);
                startActivity(myntent);
                //finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent myntent = new Intent(MainActivity.this, UpPictureActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("serverIP", serverIP);
            bundle.putString("client",client);
            myntent.putExtras(bundle);
            startActivity(myntent);
            //  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //公告栏初始化
    private void setBulletin() {
        mStringArray.add(ad);
        mEventHandler = new EventHandler(this);
        mEventHandler.sendEmptyMessageDelayed(FLAG, 1000);
        mAutoTextView = (AutoTextView) findViewById(R.id.id_main_switcher);
        mAutoTextView.setText("公告栏开始工作");
    }

    public void setSearch() {
        mEdit = (EditText) this.findViewById(R.id.edittext);
        mButton = (Button) this.findViewById(R.id.bt_search);
        mButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);  // , 1是可选写的
                String co = mEdit.getText().toString();
                Toast.makeText(getApplicationContext(), "开始搜索~", Toast.LENGTH_SHORT).show();
                switch (co) {
                    case " ":
                        Toast.makeText(getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();

                        break;
                    case "水果":
                        Toast.makeText(getApplicationContext(), "请根据标志位置自行判断，水果区反正就在那附近啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_fruit);

                        lp.setMargins(90, 400, 300, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "桃子":
                    case "葡萄":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_fruit);
                        lp.setMargins(90, 400, 300, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "苹果":
                    case "梨":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_fruit);
                        lp.setMargins(90, 500, 300, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "西瓜":
                    case "香蕉":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_fruit);
                        lp.setMargins(90, 600, 300, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "生鲜":
                        Toast.makeText(getApplicationContext(), "请根据标志位置自行判断，生鲜区反正就在那附近啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_fish);
                        lp.setMargins(1000, 400, 10, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "鱼":
                    case "虾":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        lp.setMargins(1000, 400, 10, 100);
                        map.setImageResource(R.mipmap.map_fish);
                        marker.setLayoutParams(lp);
                        break;
                    case "牛肉":
                    case "猪肉":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_fish);
                        lp.setMargins(1000, 500, 10, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "鸡肉":
                    case "羊肉":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_fish);
                        lp.setMargins(1000, 600, 10, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "日用品":
                        Toast.makeText(getApplicationContext(), "请根据标志位置自行判断，日用品区反正就在那附近啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_food);
                        lp.setMargins(1000, 780, 10, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "肥皂":
                    case "纸巾":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_food);
                        lp.setMargins(1000, 780, 10, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "牙刷":
                    case "牙膏":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_food);
                        lp.setMargins(1000, 880, 10, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "沐浴露":
                    case "洗发水":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_food);
                        lp.setMargins(1000, 980, 10, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "零食":
                        Toast.makeText(getApplicationContext(), "请根据标志位置自行判断，零食区反正就在那附近啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_day);
                        lp.setMargins(90, 780, 300, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "薯片":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_day);
                        lp.setMargins(90, 780, 300, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "饼干":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_day);
                        lp.setMargins(90, 880, 300, 100);
                        marker.setLayoutParams(lp);
                        break;
                    case "巧克力":
                        Toast.makeText(getApplicationContext(), "已定位到其所在货架，稍微看看就能找到啦！", Toast.LENGTH_SHORT).show();
                        map.setImageResource(R.mipmap.map_day);
                        lp.setMargins(90, 980, 300, 100);
                        marker.setLayoutParams(lp);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "没有搜索到结果", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


    }

    public void setButtonActivity() {
        button_fruit = (Button) findViewById(R.id.fruit);
        button_fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sheet = 0;
                map.setImageResource(R.mipmap.map);
                Intent myntent = new Intent(MainActivity.this, AdActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("pathroot", pathroot);
                bundle.putInt("sheet", 0);
                myntent.putExtras(bundle);
                startActivity(myntent);
                //finish();
            }
        });
        button_meat = (Button) findViewById(R.id.meat);
        button_meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sheet = 0;
                map.setImageResource(R.mipmap.map);
                Intent myntent = new Intent(MainActivity.this, AdActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("pathroot", pathroot);
                bundle.putInt("sheet", 1);
                myntent.putExtras(bundle);
                startActivity(myntent);
                //finish();
            }
        });


        button_daily = (Button) findViewById(R.id.daily);
        button_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sheet = 0;
                map.setImageResource(R.mipmap.map);
                Intent myntent = new Intent(MainActivity.this, AdActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("pathroot", pathroot);
                bundle.putInt("sheet", 2);
                myntent.putExtras(bundle);
                startActivity(myntent);
                //finish();
            }
        });

        button_other = (Button) findViewById(R.id.other);
        button_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sheet = 0;
                map.setImageResource(R.mipmap.map);
                Intent myntent = new Intent(MainActivity.this, AdActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("pathroot", pathroot);
                bundle.putInt("sheet", 3);
                myntent.putExtras(bundle);
                startActivity(myntent);
                //finish();
            }
        });
    }

    void donwloadpic(String ip) {
        int cnt = 4;
        ArrayList workers = new ArrayList();
        for (int i = 0; i < cnt; i++) {
            try {
                n = i;
                String picend = ".jpg";
                switch (n) {
                    case 0:
                        picend = ".png";
                        break;
                    case 1:
                        picend = ".jpg";
                        break;
                    case 2:
                        picend = ".jpg";
                        break;
                    case 3:
                        picend = ".gif";
                        break;
                }
                String url1 = "http://" + ip + "/goodspic/" + n + picend;
                String fileName1 = n + picend;
                String filePath1 = pathroot + fileName1;
                this.url = url1;
                this.filePath = filePath1;
                // Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
                // Toast.makeText(getApplicationContext(), filePath, Toast.LENGTH_SHORT).show();
                Thread worker = new Thread(networkTask);
                worker.start();
                workers.add(worker);
                worker.join(); // 这里表示等待它完成。

            } catch (Exception e) {
                System.out.println("Exception from main");
            }
        }

    }

    public static class EventHandler extends Handler {

        private WeakReference<MainActivity> wr;

        public EventHandler(MainActivity r) {
            wr = new WeakReference<MainActivity>(r);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            MainActivity activity = wr.get();
            switch (msg.what) {
                case 1:
                     int i = activity.mLoopCount % activity.mStringArray.size();
                    activity.mAutoTextView.next();
                    activity.mAutoTextView.setText(activity.mStringArray.get(i));
                    activity.mLoopCount++;
                    activity.mEventHandler.sendEmptyMessageDelayed(FLAG, 1000);
                    break;
            }
        }

    }


}

