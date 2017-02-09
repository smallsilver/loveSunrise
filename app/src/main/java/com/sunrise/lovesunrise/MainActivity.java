package com.sunrise.lovesunrise;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sunrise.lovesunrise.act.AndroidSurfaceviewExample;
import com.sunrise.lovesunrise.act.EventTestActivity;
import com.sunrise.lovesunrise.act.ViewTest;
import com.sunrise.lovesunrise.util.aqi.AirUtil;
import com.sunrise.lovesunrise.webview.H5Activity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AirUtil.AirCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent("second.process");
                //startActivity(intent);
                //Snackbar.make(view, "Replace with your own action!", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent i = new Intent(MainActivity.this, H5Activity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView text = (TextView) findViewById(R.id.main_text);
        text.setText(Html.fromHtml("<font color='#ffff00'>北京 晴 </font><font color='#ff0000'><small>2/-8℃</small></font> 不适合钓鱼"));
        text.setText(new A().getStr());
        //PlusMinusView test1 = (PlusMinusView)findViewById(R.id.test1);
        //test1.setPlusMinusOnClickListener(new PlusMinusView.PlusMinusOnClickListener() {
        //    @Override public boolean onClickAdd(View view) {
        //
        //        return true;
        //    }
        //
        //    @Override public boolean onClickMinus(View view) {
        //
        //        return true;
        //    }
        //});
        text.setOnClickListener(this);
        //Picasso.with(getApplicationContext()).load().into();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
        ImageView img = (ImageView) findViewById(R.id.main_img);
        ImageLoader.getInstance().displayImage("http://d.hiphotos.baidu.com/zhidao/pic/item/e7cd7b899e510fb38cc9205dd833c895d0430c23.jpg", img);


    }

    private void executeWeather() {
//        http://tqtdata.weathercn.com/?client=android&FKEY=f040b35c7322f992d4df2f71803847fd&weathertype=mergedata&clientVersion=6.0&osVersion=6.0.1&stationid=101010200.html
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("http://tqtdata.weathercn.com/?client=android&FKEY=f040b35c7322f992d4df2f71803847fd&weathertype=mergedata&clientVersion=6.0&osVersion=6.0.1&stationid=101010200.html")
                .build();
//new call
        Call call = mOkHttpClient.newCall(request);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                byte[] result = response.body().bytes();
                //String htmlStr =  response.body().string();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_text:
//                Intent intent = new Intent(this,SlidingTabActivity.class);
//                startActivity(intent);
//                openSurfaceTest();
//                openCamera();
                openTestEvent();
//                try {
//                    AirUtil.getData(this, UUID.randomUUID().toString(), Build.TYPE);
//                    Thread.sleep(500);
//                    AirUtil.getAlert(this);
//                    Thread.sleep(500);
//                    AirUtil.getBackground(this);
//                    Thread.sleep(500);
//                    AirUtil.GetPredict(this);
//                    Thread.sleep(500);
//                    AirUtil.getMessage(this);
//                    Thread.sleep(500);
//                    AirUtil.getVer(this);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                break;
        }
    }

    private void openSurfaceTest() {

        Intent intent = new Intent(this, ViewTest.class);
        startActivity(intent);
    }

    private void openCamera() {

        Intent intent = new Intent(this, AndroidSurfaceviewExample.class);
        startActivity(intent);
    }

    private void openTestEvent(){
        Intent intent = new Intent(this, EventTestActivity.class);
        startActivity(intent);

    }

    @Override
    public void onResult(String res) {
        Log.d(MainActivity.class.getName(), res+"\n");
    }

    @Override
    public void onResultError(Throwable ex) {

    }
}
