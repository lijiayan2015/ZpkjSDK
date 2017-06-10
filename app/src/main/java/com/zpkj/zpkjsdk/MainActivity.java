package com.zpkj.zpkjsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zpkj.okhttputils.LogUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtils.log("hello sdk");
    }
}
