package com.zpkj.tools.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zpkj.tools.statusbarUtils.StatusBarUtils;
import com.zpkj.tools.R;
import com.zpkj.tools.view.Navigation;


public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private LinearLayout parentLinearLayout;

    private Navigation nav;

    private GestureDetector myDectector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGestureDetector();
        initContentView(R.layout.activity_base);
        StatusBarUtils.initSystemBar(this, getTranslucentColorResId());
        nav = (Navigation) findViewById(R.id.nav);
        nav.setNavBg(getNavColor());
        nav.setNavOnClickListener(new Navigation.SimpleNavClickListenerImpl() {
            @Override
            public void onLeftClick(View view) {
                navFinish();
            }
        });


    }

    protected Navigation getNav() {
        return nav;
    }

    private void initGestureDetector() {
        if (myDectector == null) {
            myDectector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (e2 != null && e1 != null)
                        if (e2.getX() - e1.getX() > 0
                                && (e1.getX() >= 0 && e1.getX() <= 100)) {
                            if (Math.abs(e2.getX() - e1.getX()) > Math.
                                    abs(e2.getY() - e1.getY())
                                    && Math.abs(velocityX) > 500) {
                                finish();
                            }
                        }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return myDectector.onTouchEvent(event);
    }


    protected void navFinish() {
        finish();
    }

    public void setTilte(String title) {
        nav.setTitle(title);
    }

    public void setNavView(View left, View center, View right) {
        nav.setNavleft(left);
        nav.setNavCenter(center);
        nav.setNavRight(right);
    }

    protected abstract int getTranslucentColorResId();

    protected abstract int getNavColor();

    private void initContentView(int layoutResID) {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parentLinearLayout);
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        parentLinearLayout.addView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);
    }

    @Override
    public void setContentView(View view) {

        parentLinearLayout.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        parentLinearLayout.addView(view, params);
    }

}
