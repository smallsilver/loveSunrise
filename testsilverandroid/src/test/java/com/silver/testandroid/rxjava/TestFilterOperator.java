package com.silver.testandroid.rxjava;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @PACKAGE com.silver.testandroid.rxjava
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 2/10/17 18:05
 * @VERSION V1.0
 */
public class TestFilterOperator {

    @Test
    public void testFilterOperator() throws InterruptedException {
        Observable.just(1,2,3,4,5).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer < 4;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("call --> "+integer);
            }
        });
        System.out.println();
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for(int i=0;i<10;i++){
                        subscriber.onNext(i);
                        Thread.sleep(i*100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        })
        .debounce(300, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("debounce call --> "+integer);
            }
        });
        System.out.println();
        final Random random = new Random();
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0;i<10;i++){
                    subscriber.onNext(random.nextInt(3));
                }
            }
        }).distinct().observeOn(Schedulers.io()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("distinct call --> "+integer);
            }
        });
        System.out.println();
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for(int i=1;i<5;i++){
                        subscriber.onNext(i);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).sample(300,TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("sample call --> "+integer);
            }
        });
        System.out.println();
        Observable.interval(1000,TimeUnit.MILLISECONDS).skip(2)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println("skip call -->"+aLong);
                    }
                });

        System.out.println();
        Observable.interval(1000,TimeUnit.MILLISECONDS).take(2)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println("take call -->"+aLong);
                    }
                });

        Thread.sleep(5999);
    }
}
