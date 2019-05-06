package com.example.millie.smartshoppingmall_yly.func;

/**
 * Created by Millie on 2016/5/22.
 */

import android.os.Environment;
import android.util.Xml;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


public class ReadFile {

    static StringBuffer lsb = new StringBuffer();
    private static String htmlPath;
    private static File myFile;
    private static FileOutputStream output;


    public   String getXSSFWorkbook() throws IOException {
        String ss = "";
        String path = android.os.Environment.getExternalStorageDirectory() + "/fruits.xls";
        InputStream stream = new FileInputStream(path);
        Workbook wb = null;


        wb = new XSSFWorkbook(stream);


        Sheet sheet1 = wb.getSheetAt(0);
        for (Row row : sheet1) {
            for (Cell cell : row) {
                ss = cell.getStringCellValue() + "  ";

            }
            ss += "/n";
        }
        return ss;
    }

    public StringBuffer readXLSX() throws Exception {
        myFile = new File(htmlPath);
        output = new FileOutputStream(myFile);
        lsb.append("<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>");
        lsb.append("<head><meta http-equiv=Content-Type content='text/html; charset=utf-8'><meta name=ProgId content=Excel.Sheet>");

        String path = android.os.Environment.getExternalStorageDirectory() + "/fruits.xls";
        String str = "";
        String v = null;
        boolean flat = false;
        List<String> ls = new ArrayList<String>();
        try {
            ZipFile xlsxFile = new ZipFile(new File(path));
            ZipEntry sharedStringXML = xlsxFile.getEntry("xl/sharedStrings.xml");
            InputStream inputStream = xlsxFile.getInputStream(sharedStringXML);
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(inputStream, "utf-8");
            int evtType = xmlParser.getEventType();
            lsb.append("<table width=\"100%\" style=\"border:1px solid #000;border-width:1px 0 0 1px;margin:2px 0 2px 0;border-collapse:collapse;\">");
            lsb.append("<tr height=\" style=\"border:1px solid #000;border-width:0 1px 1px 0;margin:2px 0 2px 0;\">");
            lsb.append("<td style=\"border:1px solid #000; border-width:0 1px 1px 0;margin:2px 0 2px 0; ");
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParser.getName();
                        if (tag.equalsIgnoreCase("t")) {
                            ls.add(xmlParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                evtType = xmlParser.next();
            }
            ZipEntry sheetXML = xlsxFile.getEntry("xl/worksheets/sheet1.xml");
            InputStream inputStreamsheet = xlsxFile.getInputStream(sheetXML);
            XmlPullParser xmlParsersheet = Xml.newPullParser();
            xmlParsersheet.setInput(inputStreamsheet, "utf-8");
            int evtTypesheet = xmlParsersheet.getEventType();
            while (evtTypesheet != XmlPullParser.END_DOCUMENT) {
                switch (evtTypesheet) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParsersheet.getName();
                        if (tag.equalsIgnoreCase("row")) {
                        } else if (tag.equalsIgnoreCase("c")) {
                            String t = xmlParsersheet.getAttributeValue(null, "t");
                            if (t != null) {
                                flat = true;
                                System.out.println(flat + "有");
                            } else {
                                System.out.println(flat + "没有");
                                flat = false;
                            }
                        } else if (tag.equalsIgnoreCase("v")) {
                            v = xmlParsersheet.nextText();
                            if (v != null) {
                                if (flat) {
                                    lsb.append(str += ls.get(Integer.parseInt(v)) + "</td>");
                                } else {
                                    lsb.append(str += v + " </td> ");
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlParsersheet.getName().equalsIgnoreCase("row")
                                && v != null) {
                            lsb.append(str += "</tr>");
                        }
                        break;
                }
                evtTypesheet = xmlParsersheet.next();
            }
            output.write(lsb.toString().getBytes());
            System.out.println(str);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        if (str == null) {
            str = "解析文件出现问题";
        }
        return lsb;
    }

    public void makeFile() {
        String sdStateString = android.os.Environment.getExternalStorageState();//获取外部存储状态
        if (sdStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {//确认sd卡存在,原理不知,媒体安装??
            try {
                File sdFile = android.os.Environment.getExternalStorageDirectory();//获取扩展设备的文件目录
                String path = sdFile.getAbsolutePath() + File.separator + "xiao";//得到sd卡(扩展设备)的绝对路径+"/"+xiao
                File dirFile = new File(path);//获取xiao文件夹地址
                if (!dirFile.exists()) {//如果不存在
                    dirFile.mkdir();//创建目录
                }
                File myFile = new File(path + File.separator + "my.html");//获取my.html的地址
                if (!myFile.exists()) {//如果不存在
                    myFile.createNewFile();//创建文件
                }
                this.htmlPath = myFile.getAbsolutePath();//返回路径
            } catch (Exception e) {
            }
        }
    }


}


