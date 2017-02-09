package com.sunrise.lovesunrise.util.aqi;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @PACKAGE com.sunrise.lovesunrise.util.aqi
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 12/16/16 15:48
 * @VERSION V1.0
 */
public class AirUtil {

    // WSDL文档中的命名空间
    private static final String targetNameSpace = "http://tempuri.org/";
    // WSDL文档中的URL
    private static final String WSDL = "http://mobile.bjmemc.com.cn/AirService/Service.asmx?wsdl";

    public static interface AirCallBack {
        public void onResult(String res);

        public void onResultError(Throwable ex);
    }


    public static void GetPredict(AirCallBack callBack) {
        getAir("GetPredict", callBack);
    }

    public static void getAlert(AirCallBack callBack) {
        getAir("GetAlert", callBack);
    }

    public static void getBackground(AirCallBack callBack) {
        getAir("GetBackground", callBack);
    }

    public static void getData(AirCallBack callBack,String paramString1, String paramString2) {
        getAir("GetData", paramString1, paramString2,callBack);
    }

    public static void getMessage(AirCallBack callBack) {
        getAir("GetMessage", callBack);
    }

    public static void getVer(AirCallBack callBack) {
        getAir("GetVer", callBack);
    }

    private static void getAir(final String method, final AirCallBack callBack) {
        getAir(method,null,null,callBack);
    }
    private static void getAir(final String method, final String deviceid, final String devType, final AirCallBack callBack) {

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                AirInterface airInterface = new AirInterface();
                String airRes = "";
                if(deviceid == null && devType == null) {
                    airRes = airInterface.HttpSoap(method);
                }else{
                    airRes = airInterface.HttpSoap(method,deviceid,devType);
                }
                subscriber.onNext(airRes);//将执行结果返回
                subscriber.onCompleted();//结束异步任务
            }
        })
                .subscribeOn(Schedulers.io())//异步任务在IO线程执行
                .observeOn(AndroidSchedulers.mainThread())//执行结果在主线程运行
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onStart() {
                        //TODO 异步任务执行前的操作
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        //TODO 异步任务执行完后的操作
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO 异步任务执行失败后的操作
                        callBack.onResultError(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        //TODO 处理异步任务的执行结果
                        callBack.onResult(o == null ? null : o.toString());
                    }
                });
    }

}
