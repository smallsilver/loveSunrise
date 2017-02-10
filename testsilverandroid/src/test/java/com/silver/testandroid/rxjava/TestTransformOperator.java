package com.silver.testandroid.rxjava;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * @PACKAGE com.silver.testandroid.rxjava
 * @DESCRIPTION:
 * 最后总结下map，flatmap，contactmap，switchmap的使用场景区别：
 * map用于不需要通过操作符就可以完成的转换，
 * flatmap用于需要通过操作符才可以转换的操作，但是输出结果可能会是乱序，
 * contactmap的作用在flatmap的基础上保证输出结果按照输入顺序输出，
 * switchmap的作用在flatmap的基础上，对输出结果若同时发生，只会保证最新结果而放弃旧数据。
 * @AUTHOR dongen_wang
 * @DATE 2/10/17 16:48
 * @VERSION V1.0
 */
public class TestTransformOperator {
    
    @Test
    public void testMapOperator(){

        Observable.just("1","2","3").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s+"+map+";
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("call item->"+s);
            }
        });

        Observable.just("1","2","3").flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                int delay = 100;
                if(s.equals("2")){
                    delay = 1000;
                }else if(s.equals("3")){
                    delay = 500;
                }
                return Observable.just(s+"+flatmap+").delay(delay, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("call item->"+s);
            }
        });

        Observable.just(10, 20, 30).concatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                //10的延迟执行时间为200毫秒、20和30的延迟执行时间为180毫秒
                int delay = 200;
                if (integer == 10)
                    delay = 1800;
                else if(integer == 20)
                    delay = 500;

                return Observable.from(new Integer[]{integer, integer / 2}).delay(delay, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("concatMap Next:" + integer);
            }
        });
        Observable.just(10, 20, 30,10).switchMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                //10的延迟执行时间为200毫秒、20和30的延迟执行时间为180毫秒
                int delay = 200;
                if (integer > 10)
                    delay = 180;

                return Observable.from(new Integer[]{integer, integer / 2}).delay(delay, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("switchMap Next:" + integer);
            }
        });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer numbers[]={1,2,3,4,5,};
        Observable observable=Observable.from(numbers);

        observable.scan(new Func2<Integer,Integer,Integer>() {
            @Override
            public Integer call(Integer sum, Integer item) {
                System.out.println(" sum: "+sum+"  item: "+item);
                return sum+item;
            }
        })
        .subscribe(new Action1() {
            @Override
            public void call(Object o) {
                System.out.println(" number: "+o);
            }
        });
        final Random random = new Random();
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0;i<6;i++){
                    int number=random.nextInt(10);
                    System.out.println("key: " + number);
                    subscriber.onNext(number);
                }
            }
        }).groupBy(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return integer % 2 == 0? "偶数":"奇数";
            }
        }).subscribe(new Action1<GroupedObservable<String, Integer>>() {
            @Override
            public void call(final GroupedObservable<String, Integer> stringIntegerGroupedObservable) {
                stringIntegerGroupedObservable.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("key:" + stringIntegerGroupedObservable.getKey() +", value:" + integer);
                    }
                });
            }
        });

        Observable.interval(1, TimeUnit.SECONDS).take(10).
                window(2,TimeUnit.SECONDS).subscribe(new Action1<Observable<Long>>() {
            @Override
            public void call(Observable<Long> longObservable) {

                System.out.println("每隔两秒打印.....");
                longObservable.subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println("currentNumber: "+aLong);
                    }
                });
            }
        });

        Integer[] strs=new Integer[]{1,2,3,4,5};
        Observable.from(strs).observeOn(Schedulers.io()).cast(Long.class).subscribe(new Action1<Long>() {
            @Override
            public void call(Long integer) {
                System.out.println("integer: " + integer);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
