package com.silver.testandroid.rxjava;

import android.util.Log;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @PACKAGE com.silver.testandroid.rxjava
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 2/9/17 23:47
 * @VERSION V1.0
 */
public class TestMergeConcatFunc {

    private static String TAG = TestMergeConcatFunc.class.getName();

    List<String> list = new ArrayList<>();

    //    explain in http://reactivex.io/documentation/operators/merge.html
    @Test
    public void testRxJavaMerge(){
        Observable.merge(createObservable("http://static.diaoyu123.cc/app/default/img/icon/footer_menu/home_n_2x.png")
                ,createObservable("http://static.diaoyu123.cc/app/default/img/icon/footer_menu/home_h_2x.png"))
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
//                        Log.d(TAG,"done loading all data"+ Arrays.toString(list.toArray()));
                        System.out.println("done loading all data"+ Arrays.toString(list.toArray()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"error");
                    }

                    @Override
                    public void onNext(Object data) {
                        list.add(data.toString());
//                        Log.d(TAG,"all merged data will pass here one by one!"+data);
                        System.out.println("all merged data will pass here one by one!"+data);
                    }
                });
    }

    //    explain in http://reactivex.io/documentation/operators/concat.html
    @Test
    public void testRxJavaConcat(){
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
//                        Log.d(TAG,"testRxJavaConcat done loading all data");
                        System.out.println("testRxJavaConcat done loading all data");
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.d(TAG,"testRxJavaConcat error");
                        System.out.println("testRxJavaConcat error");
                    }

                    @Override
                    public void onNext(Object data) {
//                        Log.d(TAG,"testRxJavaConcat all concat data will pass here one by one!");
                        System.out.println("testRxJavaConcat all concat data will pass here one by one!");
                    }
                });
    }

    private Observable createObservable(final String path){

        Observable observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                try {
                    File file = new File("123.txt");
//                    File file = Glide.with(Application.class)
//                            .load(path)
//                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                            .get();
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
