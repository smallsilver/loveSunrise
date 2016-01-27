///*
// *
// * Created by smallsilver on 1/6/16 3:19 PM
// * Email dongen_wang@163.com
// *
// * Copyright 2016 SmallSilver Inc.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// *
// */
//
//package com.silver.droid.util;
//
//import android.content.Context;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.lchr.diaoyu.R;
//
///**
// * @author dongen_wang@163.com
// * @ClassName: ToastUtil
// * @Description: 统一Toast请求
// * @date 2015年3月14日 下午5:34:46
// */
//public class ToastUtil {
//
//    private static Toast toast;
//
//    public static void show(Context ctx, String message) {
//
//        LayoutInflater inflate = (LayoutInflater)
//                ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout = inflate.inflate(R.layout.toast, null);
////        layout.getBackground().setAlpha(200); //0~255透明度值
//        TextView textView = (TextView) layout.findViewById(R.id.toast_message);
//        textView.setText(message);
//        toast = new Toast(ctx);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(layout);
//        toast.show();
////
////        toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
////        show(ctx,toast);
//    }
//
//    public static void show(Context ctx, int message) {
//        show(ctx, ctx.getResources().getString(message));
////        toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
////        show(ctx,toast);
//    }
//
//}
