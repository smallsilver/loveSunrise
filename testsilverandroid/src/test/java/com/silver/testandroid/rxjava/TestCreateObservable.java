package com.silver.testandroid.rxjava;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

/**
 * @PACKAGE com.silver.testandroid.rxjava
 * @DESCRIPTION: the explain is
 * http://reactivex.io/documentation/operators/defer.html
 * http://reactivex.io/documentation/operators/create.html
 * @AUTHOR dongen_wang
 * @DATE 2/9/17 23:14
 * @VERSION V1.0
 */
public class TestCreateObservable {

    private int len = 5;
//    test create
    @Test
    public void testCreate(){
        Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        for (int i = 1; i < len; i++) {
                            System.out.println("i-->"+i);
                            subscriber.onNext(i);
                        }
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
        len = 6;
        observable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer item) {
                System.out.println("Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Sequence complete.");
            }
        });
    }

    @Test
    public void testDefer(){
        Observable<Integer> observable = Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                List<Integer> intArr = new ArrayList<>();
                for(int i=0;i<len;i++){
                    System.out.println("i-->"+i);
                    intArr.add(i);
                }
                Integer[] res = new Integer[len];
                return Observable.from(intArr.toArray(res));
            }
        });

        len = 6;
        observable.subscribe(new Subscriber<Integer>() {

            @Override
            public void onNext(Integer item) {
                System.out.println("Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Sequence complete.");
            }
        });
    }
}
