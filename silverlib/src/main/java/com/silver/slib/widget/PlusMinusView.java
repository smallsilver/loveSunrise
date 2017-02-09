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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.silver.slib.R;

import static com.silver.slib.R.id.btn_add;

/**
 * @PACKAGE com.silver.droid.common.widget
 * @DESCRIPTION: 系统购物车添加减少的布局
 * @AUTHOR dongen_wang
 * @DATE 1/6/16 12:38
 * @VERSION V1.0
 */
public class PlusMinusView extends RelativeLayout implements View.OnClickListener {

    //事件控制接口
    public static interface PlusMinusOnClickListener{
        public boolean onClickAdd(View view);
        public boolean onClickMinus(View view);
    }

    private LayoutInflater inflate;

    private ImageView plus,minus;

    private TextView si_pm_number;

    private int num = 0;

    private PlusMinusOnClickListener plusMinusOnClickListener;

    public void setPlusMinusOnClickListener(PlusMinusOnClickListener plusMinusOnClickListener) {
        this.plusMinusOnClickListener = plusMinusOnClickListener;
    }

    public PlusMinusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    public PlusMinusView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PlusMinusView(Context context) {
        this(context,null);
    }

    private void init(Context context, AttributeSet attrs, int defStyle){
        inflate = LayoutInflater.from(context);
        inflate.inflate(R.layout.si_plus_minus_layout,this,true);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        plus = (ImageView) findViewById(btn_add);
        minus = (ImageView) findViewById(R.id.btn_subtract);
        si_pm_number = (TextView) findViewById(R.id.si_pm_number);
        minus.setEnabled(false);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
    }

    public void enablePlus(boolean enable){
        plus.setEnabled(enable);
    }

    public void enableMinus(boolean enable){
        minus.setEnabled(enable);
    }

    @Override public void onClick(View v) {
        boolean clickR = false;
        int id = v.getId();
        if(id == R.id.btn_add) {
            clickR = plusMinusOnClickListener.onClickAdd(v);
            if (clickR) {
                num++;
                si_pm_number.setText(num + "");
                enableMinus(true);
            }
        }else if(id == R.id.btn_subtract) {
            clickR = plusMinusOnClickListener.onClickMinus(v);
            if(clickR){
                num--;
                si_pm_number.setText(num + "");
                if(num == 0){
                    enableMinus(false);
                }
            }
        }
    }
}
