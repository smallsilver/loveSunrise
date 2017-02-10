package com.silver.testandroid.rxjava;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
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
        System.err.println();
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

    @Test
    public void createEmptyNeverThrow(){
        Observable.empty().subscribe(new Subscriber<Object>() {
            @Override
            public void onNext(Object item) {
                System.out.println("Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("empty Sequence complete.");
            }
        });
        System.err.println();
        Observable.never().subscribe(new Subscriber<Object>() {
            @Override
            public void onNext(Object item) {
                System.out.println("Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("never Sequence complete.");
            }
        });
        System.err.println();
        Observable.error(new RuntimeException("test Run Exception")).subscribe(new Subscriber<Object>() {
            @Override
            public void onNext(Object item) {
                System.out.println("Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("error Sequence complete.");
            }
        });
        final ExecutorService executor = Executors.newFixedThreadPool( 5 );

        System.err.println();
        Observable.from(executor.submit(new Callable<String>(){
            @Override
            public String call()
            {
                return "test-->call";
            }
        })
        ).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String item) {
                System.out.println("Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("from FutureTask Sequence complete.");
            }
        });
        System.err.println();
        Observable.interval(0,1,java.util.concurrent.TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onNext(Long item) {
                System.out.println("interval Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("interval Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("interval Sequence complete.");
            }
        });

//        just
        System.err.println();
        Observable.just(1, 2, 3).subscribe(new Subscriber<Integer>() {
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
        System.err.println();
        Observable.range(2, 5).subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer item) {
                System.out.println("range Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("range Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("range Sequence complete.");
            }
        });

        System.err.println();
        Observable.just("110").repeat(10).subscribe(new Action1<String>() {
            @Override
            public void call(String item) {
                System.out.println("range Next: " + item);
            }
        });
        System.err.println();
        Observable.range(6,10).startWith(5).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer item) {
                System.out.println("range startWith Next: " + item);

            }
        });
        System.err.println();
        Observable.just(7899l).delay(2l, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long item) {
                System.out.println("delay Next: " + item);
            }
        });
        System.err.println();
        Observable obs = Observable.just(234l);
        obs.subscribe(new Action1<Long>() {
            @Override
            public void call(Long item) {
                System.out.println("timer Next: "+item);
            }
        });
        obs.timer(3l,TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long item) {
                System.out.println("timer Next: "+item);
            }
        });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
