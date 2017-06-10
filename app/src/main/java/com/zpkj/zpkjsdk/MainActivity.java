package com.zpkj.zpkjsdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zpkj.tools.LogUtils.LogUtils;
import com.zpkj.tools.net.OkHttpUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpUtils instance = OkHttpUtils.getInstance();
        LogUtils.e(instance.getClass().getSimpleName());
    }
}
