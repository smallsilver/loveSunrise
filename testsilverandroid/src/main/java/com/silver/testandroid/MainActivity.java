package com.silver.testandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            testRxJavaMerge();
            testRxJavaConcat();
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

    List<String> list = new ArrayList<>();

    //    explain in http://reactivex.io/documentation/operators/merge.html
    private void testRxJavaMerge(){
        Observable.merge(createObservable("http://static.diaoyu123.cc/app/default/img/icon/footer_menu/home_n_2x.png")
                ,createObservable("http://static.diaoyu123.cc/app/default/img/icon/footer_menu/home_h_2x.png"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG,"done loading all data"+ Arrays.toString(list.toArray()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"error");
                    }

                    @Override
                    public void onNext(Object data) {
                        list.add(data.toString());
                        Log.d(TAG,"all merged data will pass here one by one!"+data);
                    }
                });
    }
//    explain in http://reactivex.io/documentation/operators/concat.html
    private void testRxJavaConcat(){
        Observable.concat(Observable.just(1),Observable.just(2),Observable.just(3),Observable.just(4))
            .flatMap(new Func1<Integer, Observable<?>>() {
                @Override
                public Observable<String> call(Integer integer) {
                    return Observable.just(""+(integer+2));
                }
            })
            .subscribe(new Subscriber<Object>() {
                @Override
                public void onCompleted() {
                    Log.d(TAG,"testRxJavaConcat done loading all data");
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG,"testRxJavaConcat error");
                }

                @Override
                public void onNext(Object data) {
                    Log.d(TAG,"testRxJavaConcat all concat data will pass here one by one!");
                }
            });
    }

    private Observable createObservable(final String path){

        Observable observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                try {
                    File file = Glide.with(getApplicationContext())
                            .load(path)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    return Observable.just(file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    return Observable.just("");
                }
            }
        }).subscribeOn(Schedulers.io());

        return observable;
    }
}
