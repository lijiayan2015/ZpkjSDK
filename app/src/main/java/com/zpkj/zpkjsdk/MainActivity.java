package com.zpkj.zpkjsdk;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.zpkj.tools.LogUtils.LogUtils;
import com.zpkj.tools.activity.BaseActivity;
import com.zpkj.tools.net.OkHttpUtils;
import com.zpkj.tools.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private CustomProgressDialog customProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpUtils instance = OkHttpUtils.getInstance();
        LogUtils.e(instance.getClass().getSimpleName());
        initDialog();
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGet("");
            }
        });

        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDownFile();
            }
        });
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


    private void testGet(String url) {
        OkHttpUtils.doGet(url, new OkHttpUtils.ResultCallBack<String>() {

            @Override
            public void onSuccess(String respose) {
                LogUtils.e("get:" + respose);
            }

            @Override
            public void onFailure(Exception e) {
                LogUtils.e("get:onFailure:" + e.getMessage());
            }
        });
    }

    private void testPost(String url, List<OkHttpUtils.Param> params) {
        OkHttpUtils.doPost(url, params, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String respose) {
                LogUtils.e("get:" + respose);
            }

            @Override
            public void onFailure(Exception e) {
                LogUtils.e("get:onFailure:" + e.getMessage());
            }
        });
    }

    private void testDownFile() {
        String url = "http://180.153.105.145/imtt.dd.qq.com/16891/8C3E058EAFBFD4F1EFE0AAA815250716.apk?mkey=593f4b32f4933ed1&f=5c12&c=0&fsname=com.tencent.mobileqq_7.1.0_692.apk&csr=1bbd&p=.apk";
        OkHttpUtils.doDownLoadFile(url, Environment.getExternalStorageDirectory().getAbsolutePath(), "qq.apk", new ArrayList<OkHttpUtils.Param>(), new ArrayList<OkHttpUtils.Param>(),
                new OkHttpUtils.ResultCallBack<String>() {
                    @Override
                    public void downLoading(int progress) {
                        LogUtils.e("progress:" + progress);
                    }

                    @Override
                    public void downFileDone(boolean result) {
                        LogUtils.e("downFileDone:" + result);
                    }

                    @Override
                    public void onSuccess(String respose) {

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
    }
}
