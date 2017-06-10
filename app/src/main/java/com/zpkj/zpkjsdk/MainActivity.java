package com.zpkj.zpkjsdk;

import android.graphics.Color;
import android.os.Bundle;

import com.zpkj.tools.LogUtils.LogUtils;
import com.zpkj.tools.activity.BaseActivity;
import com.zpkj.tools.net.OkHttpUtils;
import com.zpkj.tools.view.CustomProgressDialog;

public class MainActivity extends BaseActivity {

    private CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpUtils instance = OkHttpUtils.getInstance();
        LogUtils.e(instance.getClass().getSimpleName());
        initDialog();
    }

    private void initDialog() {
        customProgressDialog = new CustomProgressDialog(this, "正在加载...");
        customProgressDialog.show();
    }

    @Override
    protected int getTranslucentColorResId() {
        return R.color.navigation_color;
    }

    @Override
    protected int getNavColor() {
        return Color.parseColor("#242424");
    }
}
