package com.example.millie.smartshoppingmall_yly.func;

/**
 * Created by Millie on 2016/6/19.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloader {
    private URL url = null;

    /**
     * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容
     * 1.创建一个URL对象
     * 2.通过URL对象，创建一个HttpURLConnection对象
     * 3.得到InputStram
     * 4.从InputStream当中读取数据
     *
     * @param
     * @return
     */
    public String ZFLdownload(String urlstr, String Path, String FileName) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try {

           // F.getSDPATH();
            //F.createSDDir(Path);
           // F.delFileExist(FileName);
           // F.createSDFile(FileName);


            // 创建一个URL对象
            url = new URL(urlstr);
            // 创建一个Http连接
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            // 使用IO流读取数据
            buffer = new BufferedReader(new InputStreamReader(
                    urlConn.getInputStream(), "gb2312")); // 防止中文出现乱码  gb2312
            FileUitls F=new FileUitls();
            F.write2SDFromWrite(Path,FileName,buffer);

//            while ((line = buffer.readLine()) != null) {
//                sb.append(line);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 可以下载字节流文件到SD卡中
     *
     * @param urlstr  要下载文件的URI地址
     * @param Path  在SD卡上文件夹的路径
     * @param FileName  在SD卡上文件的名称
     * @return 该函数返回整型：-1代表下载失败，0代表下载成功，1代表文件已经存在
     */
    public int ZJLdownload(String urlstr, String Path, String FileName) {
        InputStream inputstream = null;
        BufferedReader buffer = null;
        try {
            FileUitls fileUitls = new FileUitls();
            System.out.println(Path + FileName);
            if (fileUitls.isFileExist(Path + FileName)) {
                return 1;
            } else {
                // 获取URI中的字节流
                inputstream = getInputStreamFromUrl(urlstr);
                // 把字节流转换成字符流
                buffer = new BufferedReader(new InputStreamReader(inputstream,
                        "gb2312")); // 防止中文出现乱码   UTF-8
                File resultFile = fileUitls.write2SDFromWrite(Path, FileName,
                        buffer);
                if (resultFile == null) {
                    return -1;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if(buffer != null)
                    buffer.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 可以下载字符流和字节流文件到SD卡中
     *
     * @param urlstr
     * @param Path
     * @param FileName
     * @return 该函数返回整型：-1代表下载失败，0代表下载成功，1代表文件已经存在
     */
    public int downFile(String urlstr, String Path, String FileName) {
        InputStream inputstream = null;
        try {
            FileUitls fileUitls = new FileUitls();
            if (fileUitls.isFileExist(Path + FileName)) {
               // fileUitls.delFileExist();
                return 1;
            } else {
                inputstream = getInputStreamFromUrl(urlstr);
                File resultFile = fileUitls.write2SDFromInput(Path, FileName,
                        inputstream);
                if (resultFile == null) {
                    return -1;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return -1;
        } finally {
            try {
                inputstream.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlstr
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    private InputStream getInputStreamFromUrl(String urlstr)
            throws MalformedURLException, IOException {
        // TODO Auto-generated method stub
        url = new URL(urlstr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConn.getInputStream();
        return inputStream;
    }
}