/*
 *
 * Created by smallsilver on 1/6/16 12:38 PM
 * Email dongen_wang@163.com
 *
 * Copyright 2016 SmallSilver Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.silver.slib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.silver.slib.R;

/**
 * @PACKAGE com.silver.droid.common.widget
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 1/6/16 12:38
 * @VERSION V1.0
 */
public class SiImageView extends ImageView {

    private int press_color;

    private boolean isCircle;

    private int width,height;

    private boolean isPress;

    private Paint paint = new Paint();

    private Paint transparentPaint = new Paint();

    public SiImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    public SiImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SiImageView(Context context) {
        this(context,null);
    }

    private void init(Context context, AttributeSet attrs, int defStyle){
        final TypedArray attributes = context
                .obtainStyledAttributes(attrs, R.styleable.SilImageView,
                        defStyle, 0);
        if (attributes != null) {

            try {

                press_color = attributes.getColor(R.styleable.SilImageView_sil_img_press_color, Color.TRANSPARENT);
                isCircle = attributes.getBoolean(R.styleable.SilImageView_sil_img_circle,false);

            }catch(Exception ex){
                ex.printStackTrace();
            }
            finally {
                attributes.recycle();
            }

            paint.setAntiAlias(true);
            paint.setColor(press_color);
        }
        setClickable(true);

        transparentPaint.setColor(Color.parseColor("#00ffffff"));
//        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        transparentPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        if(isCircle) {
            int radio = width / 2;
            if(isPress) {
                canvas.drawCircle(radio, radio, radio, paint);
            }else{
                //清屏
                canvas.drawCircle(radio,radio,radio,transparentPaint);
            }
        }else{
            if(isPress) {
                canvas.drawRect(0, 0, width, height, paint);
            }else{
                //清屏
                canvas.drawRect(0, 0, width, height, transparentPaint);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                isPress = true;
                break;
            case MotionEvent.ACTION_UP:
                isPress = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                isPress = false;
                break;
        }

        invalidate();

        return super.onTouchEvent(event);
    }
}
