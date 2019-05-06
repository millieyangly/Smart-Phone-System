package com.example.millie.smartshoppingmall_yly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


import com.example.millie.smartshoppingmall_yly.func.DownloadFile;
import com.example.millie.smartshoppingmall_yly.func.FileReadActivity;
import com.example.millie.smartshoppingmall_yly.func.HttpDownloader;
import com.example.millie.smartshoppingmall_yly.func.ReadFile;
import com.example.millie.smartshoppingmall_yly.message.WebSocketConnection;
import com.example.millie.smartshoppingmall_yly.message.WebSocketConnectionHandler;
import com.example.millie.smartshoppingmall_yly.message.WebSocketException;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/*
*import jxl.Sheet;
*import jxl.Workbook;
*/


/**
 * Created by Millie on 2016/6/19.
 */
public class oldmain extends Activity {


    //public class MainActivity extends Activity {
        static StringBuffer lsb = new StringBuffer();
        private static String htmlPath;
        private static File myFile;
        // private static FileOutputStream output;
        TextView txt = null;
        String url = "http://192.168.0.111:8080/smartshoppingmall/fruit.xlsx";//5.25
        private TextView tv;
        public TextView mg;//公告
        private WebView view;
        private int screenWidth;
        private String nameStr = null;
        private TextView tv1;
        private String picturePath;
        private int presentPicture = 0;
        // public String Path= Environment.getExternalStorageDirectory()+ "smartshoppingmall/price/";
        //  public String filePath= Environment.getExternalStorageDirectory()+ "smartshoppingmall/price/fruit.xlsx";
        public String pathroot= Environment.getExternalStorageDirectory()+ File.separator;
        public String fileName= "fruit.xlsx";
        public String filePath= pathroot+ fileName;


        private Button btn_load;
        public int sheet=1;

        //  String myString = null;
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //start
            BufferedReader buffer=null;
            FileWriter output = null;   //创建一个写入字符流对象
            BufferedWriter bufw = null;
            Toast.makeText(oldmain.this, "1", Toast.LENGTH_LONG).show();
            try {
                Toast.makeText(oldmain.this,"7", Toast.LENGTH_LONG).show();
                URL urlpath = new URL(url);
                HttpURLConnection urlConn = (HttpURLConnection) urlpath
                        .openConnection();
                urlConn.connect();
                Toast.makeText(oldmain.this,"2", Toast.LENGTH_LONG).show();
                buffer = new BufferedReader(new InputStreamReader(
                        urlConn.getInputStream(), "gb2312")); // 防止中文出现乱码  gb2312
                File file = new File(pathroot + fileName);
                if (file.exists())
                    file.delete();
                Toast.makeText(oldmain.this,"3", Toast.LENGTH_LONG).show();
                file.createNewFile();
                Toast.makeText(oldmain.this,"4", Toast.LENGTH_LONG).show();
                output = new FileWriter(file);
                bufw = new BufferedWriter(output);
                String line = null;
                while((line = (buffer.readLine())) != null){
                    // System.out.println("line = " + line);
                    bufw.write(line);
                    bufw.newLine();
                }
                bufw.flush();  //清空缓存
                Toast.makeText(oldmain.this,"5", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    bufw.close();
                    buffer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            //stop


            //下载价格表
//        HttpDownloader httpDownloader = new HttpDownloader();
//        String result = httpDownloader.ZFLdownload(
//                "http://192.168.0.111:8080/smartshoppingmall/fruit.xlsx", "smartshoppingmall/price/",
//                "fruit.xlsx");
//        System.out.println("down load txt result = " + result);




//显示公告文本
            tv = new TextView(this);
            //tv=(TextView)findViewById(R.id.textView2);


            final WebSocketConnection wsc = new WebSocketConnection();
            try {
                wsc.connect("ws://192.168.1.107:8080/websocket01/chat.ws?username=ws-android-client2", new WebSocketConnectionHandler(){
                    public void onBinaryMessage(byte[] payload) {
                        System.out.println("onBinaryMessage size="+payload.length);
                    }
                    @Override
                    public void onClose(int code, String reason) {
                        System.out.println("onClose reason="+reason);
                    }
                    @Override
                    public void onOpen() {
                        System.out.println("onOpen");
                        wsc.sendTextMessage("欢迎光临!");
//					wsc.disconnect();
                    }
                    @Override
                    public void onRawTextMessage(byte[] payload) {
                        System.out.println("onRawTextMessage size="+payload.length);
                        //	tv.setText(payload);
                    }

                    @Override
                    public void onTextMessage(String payload) {

                        tv.setText(payload);
                        System.out.println("onTextMessage"+payload);


//					Log.v("debug","onTextMessage");
                    }
                });
            } catch (WebSocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }




            //跳转页面显示价格表格
            btn_load = (Button) findViewById(R.id.bt_search);
            btn_load.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    sheet=2;
                    Intent i = new Intent();
                    i.setClass(oldmain.this, FileReadActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("filePath", filePath);
                    bundle.putInt("sheet",sheet);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
            });
//        String s;
////文件下载
//        DownloadFile DF = new DownloadFile();
//        String filename = "fruit.xlsx";
//        DF.downloadfile(url, filename);
//        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_LONG).show();
//
////文件显示
//        // readExcel();
//        try {
//
//            ReadFile rf = new ReadFile();
//
//            s = rf.getXSSFWorkbook();
//            tv.setText(s);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Toast.makeText(MainActivity.this, "2", Toast.LENGTH_LONG).show();

//
//        try {
///*定义获取文件内容的URL*/
//            URL myURL = new URL("http://192.168.3.120:8080/smartshoppingmall/fruit.xlsx");
///*打开URL连接*/
//            URLConnection ucon = myURL.openConnection();
///*使用IputStreams，从URLConnection读取数据*/
//            InputStream is = ucon.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is);
///*用ByteArrayBuffer做缓存*/
//            ByteArrayBuffer baf = new ByteArrayBuffer(50);
//            int current = 0;
//            while ((current = bis.read()) != -1) {
//                baf.append((byte) current);
//            }
///*将缓存的内容转为String，用Utf-8编码*/
//            myString = EncodingUtils.getString(baf.toByteArray(), "UTF-8");
//        } catch (Exception e) {
///*捕捉异常错误*/
//            myString = e.getMessage();
//        }
///*设置屏幕显示*/
            //  TextView textView = (TextView) findViewById(R.id.textView);
            //   textView.setText(myString);
            // this.setContentView(tv);

            //



        }


//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


//    try {
//
//            Toast.makeText(MainActivity.this,"1", Toast.LENGTH_LONG).show();
//
//          //  Toast.makeText(MainActivity.this,"2", Toast.LENGTH_LONG).show();
////            try {
/////*定义获取文件内容的URL*/
////                URL myURL = new URL("http://192.168.3.120:8080/smartshoppingmall/fruit.xlsx");
/////*打开URL连接*/
////                URLConnection ucon = myURL.openConnection();
/////*使用IputStreams，从URLConnection读取数据*/
////                InputStream is = ucon.getInputStream();
////                BufferedInputStream bis = new BufferedInputStream(is);
/////*用ByteArrayBuffer做缓存*/
////                ByteArrayBuffer baf = new ByteArrayBuffer(50);
////                int current = 0;
////                while ((current = bis.read()) != -1) {
////                    baf.append((byte) current);
////                }
/////*将缓存的内容转为String，用Utf-8编码*/
////                myString = EncodingUtils.getString(baf.toByteArray(), "UTF-8");
////            } catch (Exception e) {
/////*捕捉异常错误*/
////                myString = e.getMessage();
////            }
//
// //           /*定义获取文件内容的URL*/
//          //  URL myURL = new URL();
/////*打开URL连接*/
//       //     Toast.makeText(MainActivity.this,"3", Toast.LENGTHurl_LONG).show();
//          //  HttpURLConnection ucon = (HttpURLConnection)myURL.openConnection();
//
//            Toast.makeText(MainActivity.this,"4", Toast.LENGTH_LONG).show();
//            //FileInputStream fin;//
//         //   InputStream in=ucon.getInputStream(); //= null; // 这里是你已经存在的输入流
//            Toast.makeText(MainActivity.this,"5", Toast.LENGTH_LONG).show();
//            FileInputStream fin = null; // 转换后的文件输入流
//
//// 如果是FileInputStream类型，进行转换
//         //   if (in instanceof FileInputStream) {
//           //     fin = (FileInputStream) in;
//             //   Toast.makeText(MainActivity.this,"6", Toast.LENGTH_LONG).show();
//            //} else { // 否则，转型失败url////
//            //    throw new Exception();
//
//
//           // }
//
//            Toast.makeText(MainActivity.this,"7", Toast.LENGTH_LONG).show();
//
//            //InputStream is = fin;
//
//            InputStream is = new FileInputStream("smartshoppingmall/fruit.xls");
//           // InputStream is = new FileInputStream(bis);
//           // //Workbook book = Workbook.getWorkbook(new File("mnt/sdcard/test.xls"));
//
//            Toast.makeText(MainActivity.this,"8", Toast.LENGTH_LONG).show();
//            Workbook book = Workbook.getWorkbook(is);
//            Toast.makeText(MainActivity.this,"9", Toast.LENGTH_LONG).show();
//        //    Workbook book = Workbook.getWorkbook(in);
//            int num = book.getNumberOfSheets();
//            txt.setText("the num of sheets is " + num + "\n");
//            // 获得第一个工作表对象
//            Sheet sheet = book.getSheet(0);
//            int Rows = sheet.getRows();
//            int Cols = sheet.getColumns();
//            txt.append("the name of sheet is " + sheet.getName() + "\n");
//            txt.append("total rows is " + Rows + "\n");
//            txt.append("total cols is " + Cols + "\n");
//            for (int i = 0; i < Cols; ++i) {
//                for (int j = 0; j < Rows; ++j) {
//                    // getCell(Col,Row)获得单元格的值
//                    txt.append("contents:" + sheet.getCell(i, j).getContents() + "\n");
//                }
//            }
//            book.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }


    }

//}
