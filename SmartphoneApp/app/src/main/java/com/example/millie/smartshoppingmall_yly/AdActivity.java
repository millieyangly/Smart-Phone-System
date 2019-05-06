package com.example.millie.smartshoppingmall_yly;

/**
 * Created by Millie on 2016/6/19.
 */
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.millie.smartshoppingmall_yly.func.FR;
import com.gigamole.library.ntb.NavigationTabBar;

import java.util.ArrayList;

/**
 * Created by Shimy on 2016/6/19.
 */
public class AdActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Toolbar toolbar;
    public String pathroot = null;
    public int sheettype=0;
    FR fr=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_list);
        viewPager = (ViewPager)findViewById(R.id.vp_vertical_ntb);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_vertical);


        //toolbar
//                toolbar = (Toolbar)findViewById(R.id.toolbar2);
//        toolbar.setNavigationIcon(R.mipmap.back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
//            @Override
//        public void onClick(View v){
//                AdActivity.this.finish();
//            }
//        });
//        toolbar.setTitle("促销信息");



        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        pathroot = bundle.getString("pathroot");
        sheettype=bundle.getInt("sheet");


        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }//修改个数

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view;
                view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.item_vp, null, false);

                sheettype=position;
                String filename=pathroot+sheettype+".jpg";

             //   Toast.makeText(AdActivity.this, sheettype, Toast.LENGTH_LONG).show();
                switch (sheettype){

                    case 0:
                     filename=pathroot+sheettype+".png";break;
                    case 1:
                       filename=pathroot+sheettype+".jpg";break;
                    case 2:
                        filename=pathroot+sheettype+".jpg";break;
                    case 3:
                        filename=pathroot+sheettype+".gif";break;

                }






                fr=new FR(filename,sheettype);

                final WebView txtPage = (WebView) view.findViewById(R.id.txt_vp_item_page);

                //	tv.setText(fr.returnPath);
                txtPage.loadUrl(fr.returnPath);

               // txtPage.setText(String.format("Page #%d", position));//此处传值！

                container.addView(view);
                return view;
            }
        });

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.fruit),
                        Color.parseColor("#ffda5b")
                ).title("Heart")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.fish),
                        Color.parseColor("#ff7f5c")
                ).title("Cup")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.towel),
                        Color.parseColor("#cc9b8e")
                ).title("Diploma")
                        .badgeTitle("state")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.other),
                        Color.parseColor("#a0c9d3")
                ).title("Flag")
                        .badgeTitle("icon")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, sheettype);

    }


}
