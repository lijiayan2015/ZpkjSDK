package com.zpkj.tools.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.zpkj.tools.R;


public class CustomProgressDialog extends ProgressDialog {

    private AnimationDrawable mAnimation;
    private Context mContext;
    private ImageView mImageView;
    private String mLoadingTip;
    private TextView mLoadingTv;
    private int count = 0;
    private String oldLoadingTip;
    private int mResid;
    private int id;

    public CustomProgressDialog(Context context, String content) {
        super(context);
        this.mContext = context;
        this.mLoadingTip = content;
        this.mResid = R.drawable.loading_anim;
        setCanceledOnTouchOutside(false);
        setCancelable(true);

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置透明背景
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        mImageView.setBackgroundResource(mResid);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
        mLoadingTv.setText(mLoadingTip);
        if (mLoadingTip == null || mLoadingTip.length() == 0) {
            mLoadingTv.setVisibility(View.GONE);
        }
    }

    public CustomProgressDialog setContent(String str) {
        try {
            if (mLoadingTv != null) {
                if (str != null && str.length() > 0) {
                    mLoadingTv.setText(str);
                    mLoadingTv.setVisibility(View.VISIBLE);
                } else mLoadingTv.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private void initView() {
        setContentView(R.layout.progress_dialog);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        mImageView = (ImageView) findViewById(R.id.loadingIv);
    }


}
