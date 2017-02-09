package com.sunrise.lovesunrise.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.sunrise.lovesunrise.R;

/**
 * @PACKAGE com.sunrise.lovesunrise.webview
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 6/17/16 16:18
 * @VERSION V1.0
 */
public class H5Activity extends AppCompatActivity {

  @Override public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.sys_activity_layout);

    getSupportFragmentManager().beginTransaction()
        .add(R.id.common_fragment_content,H5ParentFragment.newInstance("http://web.demo.com/study/templates/zhibo.html","123",null)).commitAllowingStateLoss();

  }

}
