package com.example.millie.smartshoppingmall_yly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.millie.smartshoppingmall_yly.message.WebSocketConnection;
import com.example.millie.smartshoppingmall_yly.message.WebSocketConnectionHandler;
import com.example.millie.smartshoppingmall_yly.message.WebSocketException;

/**
 * Created by Millie on 2016/6/20.
 */
public class FirstActivity extends Activity {
    EditText ip;
    EditText name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        ip = (EditText) findViewById(R.id.IP);
        ip.setText("192.168.0.106");
        name = (EditText) findViewById(R.id.phone);
        name.setText("15271811363");
        //进入按钮
        Button bt = (Button) findViewById(R.id.comein);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverIP = ip.getText().toString();
                String clientname = name.getText().toString();

                if (serverIP.equals("") || clientname.equals("")) {

                    Toast.makeText(getApplicationContext(), "请填写正确用户名", Toast.LENGTH_SHORT).show();

                } else {


                    final WebSocketConnection wsc = new WebSocketConnection();
                    try {
                     //   Toast.makeText(getApplicationContext(), serverIP, Toast.LENGTH_SHORT).show();
                        System.out.println("ws://" + serverIP + ":8080/websocket01/chat.ws?username=ws-android-client-login");
                        wsc.connect("ws://" + serverIP + ":8080/websocket01/chat.ws?username=ws-android-client-login", new WebSocketConnectionHandler() {
                            //   wsc.connect("ws://192.168.0.106:8080/websocket01/chat.ws?username=ws-android-client", new WebSocketConnectionHandler(){

                            public void onBinaryMessage(byte[] payload) {
                                System.out.println("onBinaryMessage size=" + payload.length);
                            }

                            @Override
                            public void onClose(int code, String reason) {
                                System.out.println("onClose reason=" + reason);
                            }

                            @Override
                            public void onOpen() {
                                String clientname1 = name.getText().toString();
                                System.out.println("onOpen");
                                wsc.sendTextMessage(clientname1);
                                // wsc.disconnect();
                            }

                            @Override
                            public void onRawTextMessage(byte[] payload) {
                                System.out.println("onRawTextMessage size=" + payload.length);
                                //	tv.setText(payload);
                            }

                            @Override
                            public void onTextMessage(String payload) {
                                //tv.setText(payload);
                                System.out.println("onTextMessage" + payload);
                                if (payload.equals("true")) {
                                    wsc.disconnect();

                                    Toast.makeText(getApplicationContext(), "欢迎光临", Toast.LENGTH_SHORT).show();
                                    String serverIP1 = ip.getText().toString();
                                    String n = name.getText().toString();
                                    Intent myntent = new Intent(FirstActivity.this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ip", serverIP1);
                                    bundle.putString("client", n);
                                    myntent.putExtras(bundle);
                                    startActivity(myntent);
                                    finish();
                                }
                                if (payload.equals("false")) {
                                    Toast.makeText(getApplicationContext(), "账户不存在，请先注册", Toast.LENGTH_SHORT).show();
                                    ip.setText("192.168.0.106");
                                    name.setText("15271811363");
                                    wsc.disconnect();
                                }
                            }
                        });
                    } catch (WebSocketException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }



                }
            }
        });


        Button z = (Button) findViewById(R.id.zhuce);
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverIP = ip.getText().toString();
                String clientname = name.getText().toString();
                if (serverIP.equals("") || clientname.equals("")) {

                    Toast.makeText(getApplicationContext(), "请填写正确用户名", Toast.LENGTH_SHORT).show();

                } else {

                    final WebSocketConnection wsc = new WebSocketConnection();
                    try {
                        wsc.connect("ws://" + serverIP + ":8080/websocket01/chat.ws?username=ws-android-client-zhuce", new WebSocketConnectionHandler() {
                            public void onBinaryMessage(byte[] payload) {
                                System.out.println("onBinaryMessage size=" + payload.length);
                            }

                            @Override
                            public void onClose(int code, String reason) {
                                System.out.println("onClose reason=" + reason);
                            }

                            @Override
                            public void onOpen() {
                                String clientname1 = name.getText().toString();
                                System.out.println("onOpen");
                                //   wsc.sendTextMessage("register");
                                wsc.sendTextMessage(clientname1);
                                wsc.disconnect();
                            }

                            @Override
                            public void onRawTextMessage(byte[] payload) {
                                System.out.println("onRawTextMessage size=" + payload.length);
                                //	tv.setText(payload);
                            }

                            @Override
                            public void onTextMessage(String payload) {

                                System.out.println("onTextMessage" + payload);


                            }

                        });
                    } catch (WebSocketException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    String serverIP1 = ip.getText().toString();
                    String n = name.getText().toString();
                    Intent myntent = new Intent(FirstActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ip", serverIP1);
                    bundle.putString("client", n);
                    myntent.putExtras(bundle);
                    startActivity(myntent);
                    finish();
                }
            }
        });
    }

}
