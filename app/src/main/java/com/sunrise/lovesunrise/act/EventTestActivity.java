package com.sunrise.lovesunrise.act;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.sunrise.lovesunrise.R;

/**
 * @PACKAGE com.sunrise.lovesunrise.act
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 2/3/17 12:01
 * @VERSION V1.0
 */
public class EventTestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout_test);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(EventTestActivity.class.getName(),""+event.getX()+" "+event.getY()+" "+event.getRawX()+" "+event.getRawY());
        return super.onTouchEvent(event);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // 状态栏高度
        int statusBarHeight = frame.top;
        View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int contentTop = v.getTop();

        // statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - statusBarHeight;

        Log.i(EventTestActivity.class.getName(),"标题栏的高度" + Integer.toString(titleBarHeight) + "\n"
                + "状态栏高度" + statusBarHeight + "\n" + "视图的宽度" + v.getWidth()
                + "\n" + "视图的高度（不包含状态栏和标题栏）" + v.getHeight());

    }
}
