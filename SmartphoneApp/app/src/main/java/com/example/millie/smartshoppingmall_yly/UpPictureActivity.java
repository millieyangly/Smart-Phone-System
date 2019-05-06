package com.example.millie.smartshoppingmall_yly;
/**
 * Created by Millie on 2016/6/22.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.millie.smartshoppingmall_yly.message.WebSocketConnection;
import com.example.millie.smartshoppingmall_yly.message.WebSocketConnectionHandler;
import com.example.millie.smartshoppingmall_yly.message.WebSocketException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
/* import相关class */

public class UpPictureActivity extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    /*
     * 变量声明 filename：上传后在服务器上的文件名称 uploadFile：要上传的文件路径 actionUrl：服务器上对应的程序路径
     */
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    //  private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final String TAG = "uploadImage";

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
    private String susresult;
    private String uploadFile = Environment.getExternalStorageDirectory() + File.separator + "1.jpg";
    //  private String actionUrl = "http://192.168.1.21:8080/upload_file_service/upload.jsp";
    private String srcPath = Environment.getExternalStorageDirectory() + File.separator + "1.jpg";
    private String serverIP;
    private String client;
    private ImageView imageView;
    private String picPath = null;
    private String actionUrl;
    //private TextView mText1;
    //private TextView mText2;
    private EditText edittext1;
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {

            // TODO
            // 在这里进行 http request.网络请求相关操作
            //   for(n=1;n<4;n++) {
            uploadFile();
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "请求结果");
            msg.setData(data);
            handler.sendMessage(msg);


        }
    };
    private Button takephoto;
    private Button mButton;
    private Button selectImage;
    private Uri fileUri;

    /**
     * Create a file Uri for saving an image or video
     */

    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = null;
        try {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "MyCameraApp");

            //  Log.d(LOG_TAG, "Successfully created mediaStorageDir: "
            //  + mediaStorageDir);

        } catch (Exception e) {
            e.printStackTrace();
            //   Log.d(LOG_TAG, "Error in Creating mediaStorageDir: "
            //     + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // 在SD卡上创建文件夹需要权限：
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//                Log.d(LOG_TAG,
//                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uppicture);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();


        serverIP = bundle.getString("serverIP");
        client = bundle.getString("client");

        edittext1 = (EditText) findViewById(R.id.fankui);
        imageView = (ImageView) findViewById(R.id.imageView);
        actionUrl = "http://" + serverIP + "/upload_file_service/upload.jsp";

        final WebSocketConnection wsc = new WebSocketConnection();
        String ws = "ws://" + serverIP + ":8080/websocket01/chat.ws?username=ws-android-" + client;
        System.out.println(ws);

        try {
            wsc.connect(ws, new WebSocketConnectionHandler() {
                public void onBinaryMessage(byte[] payload) {
                    System.out.println("onBinaryMessage size=" + payload.length);
                }

                @Override
                public void onClose(int code, String reason) {
                    System.out.println("onClose reason=" + reason);
                }

                @Override
                public void onOpen() {
                    System.out.println("onOpen");
                    String pictxt = edittext1.getText().toString();
                    System.out.println("反馈信息：" + pictxt);
                    wsc.sendTextMessage(pictxt);
//					wsc.disconnect();
                }

                @Override
                public void onRawTextMessage(byte[] payload) {
                    System.out.println("onRawTextMessage size=" + payload.length);
                    //	tv.setText(payload);
                }

                @Override
                public void onTextMessage(String payload) {
                    //tv.setText(payload);
                    // ad = payload;
                    // setBulletin();
                    // mAutoTextView.setText(payload);

                }

            });
        } catch (WebSocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        takephoto = (Button) this.findViewById(R.id.takephoto);
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // 利用系统自带的相机应用:拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // create a file to save the image
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
                // set the image file name
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
        selectImage = (Button) this.findViewById(R.id.selectImage);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /***
                 * 这个是调用android内置的intent，来过滤图片文件   ，同时也可以过滤其他的
                 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //回调图片类使用的
                startActivityForResult(intent, RESULT_CANCELED);
            }
        });


    /* 设置mButton的onClick事件处理 */
        mButton = (Button) findViewById(R.id.myButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String pictxt = edittext1.getText().toString();
                if (pictxt == "") {
                    pictxt = "无";

                }
                System.out.println("反馈信息：" + pictxt);
                wsc.sendTextMessage(pictxt);
                Thread worker = new Thread(networkTask);
                worker.start();
                edittext1.setText("");
                imageView.setImageResource(0);
                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();

            }
        });


    }

    /* 上传文件至Server的方法 */
    private void uploadFile() {
        String uploadUrl = "http://" + serverIP + ":8080/upload_file_service/UploadServlet";
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(httpURLConnection
                    .getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos
                    .writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                            + srcPath.substring(srcPath.lastIndexOf("/") + 1)
                            + "\"" + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);

            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            System.out.println("传输结果：" + result);

            new AlertDialog.Builder(UpPictureActivity.this).setTitle("系统提示")//设置对话框标题
                   .setMessage(result);//设置显示的内容
            edittext1.setText("");

            try {
                imageView.setImageResource(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //   imageView.setImageBitmap(bitmap);
////            }
////
////    }catch () {
////        e.printStackTrace();
//   // }


            //susresult = result;

            // Toast.makeText(this, result, Toast.LENGTH_LONG).show();


            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            //  setTitle(e.getMessage());
        }

    }

    /**
     * 回调执行的方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.e(TAG, "uri = " + uri);
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            // Uri uri = data.getData();
            //Log.e(TAG, "uri = " + uri);
            try {
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);
                    /***
                     * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名
                     * 如果是图片格式的话，那么才可以
                     */
                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        picPath = path;
                        srcPath = picPath;
                        uploadFile = picPath;
                        System.out.println("Path:" + srcPath);
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        imageView.setImageBitmap(bitmap);
                    } else {
                        alert();
                    }
                } else {
                    alert();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
// 如果是拍照
//        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            Uri uri = data.getData();
//            Log.e(TAG, "uri = " + uri);
//            //   Log.d(LOG_TAG, "CAPTURE_IMAGE");
//            try {
//            String[] pojo = {MediaStore.Images.Media.DATA};
//            Cursor cursor = managedQuery(uri, pojo, null, null, null);
//            if (RESULT_OK == resultCode) {
//                //  Log.d(LOG_TAG, "RESULT_OK");
//
//                // Check if the result includes a thumbnail Bitmap
//                if (data != null) {
//                    ContentResolver cr = this.getContentResolver();
//                    int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    cursor.moveToFirst();
//                    String path = cursor.getString(colunm_index);
//                    picPath = path;
//                    srcPath = picPath;
//                    uploadFile = picPath;
//                    System.out.println("Path:" + srcPath);
//                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                    imageView.setImageBitmap(bitmap);
//
//                    // 没有指定特定存储路径的时候
////                    Log.d(LOG_TAG,
////                            "data is NOT null, file on default position.");
//
//                    // 指定了存储路径的时候（intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);）
//                    // Image captured and saved to fileUri specified in the
//                    // Intent
//                    Toast.makeText(this, "Image saved to:\n" + path,
//                            Toast.LENGTH_LONG).show();
//                }
//
//                    //   if (data.hasExtra("data")) {
//                    //     Bitmap thumbnail = data.getParcelableExtra("data");
//                //    Bitmap bitmap1 = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                //    imageView.setImageBitmap(bitmap1);
//                }}
//            catch(Exception e) {
//                e.printStackTrace();
//            }}
//                    }
//                } else {
//
////                    Log.d(LOG_TAG,
////                            "data IS null, file saved on target position.");
//                    // If there is no thumbnail image data, the image
//                    // will have been stored in the target output URI.
//
//                    // Resize the full image to fit in out image view.
//                    int width = imageView.getWidth();
//                    int height = imageView.getHeight();
//
//                    BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
//
//                    factoryOptions.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);
//
//                    int imageWidth = factoryOptions.outWidth;
//                    int imageHeight = factoryOptions.outHeight;
//
//                    // Determine how much to scale down the image
//                    int scaleFactor = Math.min(imageWidth / width, imageHeight
//                            / height);
//
//                    // Decode the image file into a Bitmap sized to fill the
//                    // View
//                    factoryOptions.inJustDecodeBounds = false;
//                    factoryOptions.inSampleSize = scaleFactor;
//                    factoryOptions.inPurgeable = true;
//
//                    Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
//                            factoryOptions);
//
//                    imageView.setImageBitmap(bitmap);
//                }
//            } else if (resultCode == RESULT_CANCELED) {
//                // User cancelled the image capture
//            } else {
//                // Image capture failed, advise user
//            }
//        }
//

        /**
         * 回调使用
         */
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                picPath = null;
                            }
                        })
                .create();
        dialog.show();
    }


}



