package com.example.millie.smartshoppingmall_yly.func;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Millie on 2016/6/8.
 * 从服务器读取
 */
public class DownloadFile {

    public void downloadfile(String fileurl, String filename) {

        //先获得输入流(比如通过URL)
        fileurl = "http://192.168.0.111:8080/smartshoppingmall/fruit.xlsx";
filename="fruit.xlsx";
        InputStream in = null;
        int length;
        String savepath=Environment.getExternalStorageDirectory().getPath()+ "/SMARTSHOPPING/" +"/UpdatePricelist";;

        try {
            URL url = new URL(fileurl);
            HttpURLConnection hcon = (HttpURLConnection) url.openConnection();

          //  hcon.setReadTimeout(3000);
            //hcon.setConnectTimeout(3000);
            hcon.connect();
            length = hcon.getContentLength();

            in = hcon.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
      // return in;

        //下载
        File filepath = new File(savepath);//文件目录
        if(!filepath.exists()){
            filepath.mkdir();
                    }
        File file=new File(savepath,filename);

       // int type = 0;
       File ex = Environment.getExternalStorageDirectory();
        try{
          //  FileOutputStream out=new FileOutputStream(file);
            FileOutputStream out = new FileOutputStream(new File(ex.getAbsolutePath()+File.separator+"fruit.xlsx"));
            int count = 0;
            byte bb[] =new byte[1024];

       //     do{
                int numread = in.read(bb);
                count += numread;
// 计算进度条位置
             //   progress = (int) (((float) count / length) * 100);
// 更新进度
               // mHandler.sendEmptyMessage(DOWNLOAD);
             //   if (numread <= 0)
               // {
// 下载完成
               //     mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
               //     break;
               // }
                // 写入文件
                out.write(bb, 0, numread);
          //  }
           // while (!cancelUpdate);// 点击取消就停止下载.
            out.close();

          //  while((len = in.read(bb))!=-1)
          //  { out.write(bb,0,len); }
           // out.close();
        }catch (Exception e){
            e.printStackTrace();
        }




//
//
//        URLConnection con = null;
//        URL theUrl = null;
//        try {
//            theUrl = new URL(url);//建立地址
//            con = theUrl.openConnection();//打开连接
//            con.setConnectTimeout(30000);
//          //  con.connect();//连接
//        } catch (MalformedURLException e) {
//            // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
//            return "给定的URL地址有误，请查看";
//        } catch (IOException e) {
//            //  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
//            return "无法连接到远程机器，请重试！";
//        }
//
//
//        // jLabel5.setText("√");
//        //   Process++;
//       /*
//        *创建目录
//        */
//        String fileurl = Environment.getExternalStorageDirectory().getPath()
//                + "/SMARTSHOPPING/" +"/UpdatePricelist";
//        File fileroot = new File(fileurl);
//
//        if (!fileroot.exists()) {
//            fileroot.mkdir();
//        }
//
//       /*
//        *创建文件
//        */
//        File file = new File(fileurl+filename);
//
//        if (file.exists())
//            file.delete();
//        try {
//            file.createNewFile();
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//        }
//
//       /*
//        *写入数据
//        */
//        String type = con.getContentType();
//        if (type != null) {
//            byte[] buffer = new byte[4 * 1024];
//            int read;
//            try {
//                FileOutputStream os = new FileOutputStream(filename);
//                InputStream in = con.getInputStream();//重定向输入
//                while ((read = in.read(buffer)) > 0) {//读取输出
//                    os.write(buffer, 0, read);//写入本地文件
//                }
//                os.close();
//                in.close();
//                //  jLabel6.setText("√");
//                //  Process++;
//            } catch (FileNotFoundException e) {
//                // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
//                return "所要下载的文件不存在！";
//            } catch (IOException e) {
//                //  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
//                return "读取远程文件时出错！";
//            }
//        } else {
//            return "文件未找着:" + url;
//        }
//        return "";
    }
}
