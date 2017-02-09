package com.sunrise.lovesunrise.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * @PACKAGE com.sunrise.lovesunrise.view
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 2/3/17 12:12
 * @VERSION V1.0
 */
public class MyTestView extends ViewGroup {

    public MyTestView(Context context) {
        super(context);
    }

    public MyTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(MyTestView.class.getName(),""+event.getX()+" "+event.getY()+" "+event.getRawX()+" "+event.getRawY());
        Log.i(MyTestView.class.getName(),""+getLeft()+" "+getRight()+" "+getTop()+" "+getBottom()+" "+getX()+" "+getY());

        return super.onTouchEvent(event);
    }
}
