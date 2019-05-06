package com.example.millie.smartshoppingmall_yly.func;

/**
 * Created by Millie on 2016/6/16.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.TableIterator;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.millie.smartshoppingmall_yly.R;

public class FileReadActivity extends Activity {
    public String nameStr = null;
    public int sheettype=1;
    public Range range = null;
    public HWPFDocument hwpf = null;
    public String htmlPath;
    public String picturePath;
    public WebView view;
    public List pictures;
    public TableIterator tableIterator;
    public int presentPicture = 0;
    public int screenWidth;
    public FileOutputStream output;
    public File myFile;
    StringBuffer lsb = new StringBuffer();
    FR fr=null;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_view_activity);
        this.view = (WebView)findViewById(R.id.wv_view);

        this.view.getSettings().setBuiltInZoomControls(true);
        this.view.getSettings().setUseWideViewPort(true);
        this.view.getSettings().setSupportZoom(true);
        this.screenWidth = this.getWindowManager().getDefaultDisplay().getWidth() - 10;//设置宽度为屏幕宽度-10

        //String filePath="";

        //	read();
        // 创建意图 获得要显示的文件
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        nameStr = bundle.getString("filePath");
        sheettype=bundle.getInt("sheet");

        fr=new FR(nameStr,sheettype);
        //	tv.setText(fr.returnPath);
        this.view.loadUrl(fr.returnPath);
    }



}
