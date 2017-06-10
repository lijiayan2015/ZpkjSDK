package com.zpkj.tools.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpkj.tools.R;


/**
 * 默认的Navigation
 *
 * @Email:lijiayan_mail@163.com
 * @created_time 2016/12/03 9:42
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG             #
 * #                                                   #
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class Navigation extends LinearLayout {

    private Context mContext;

    private LinearLayout nav_left, nav_center, nav_right;

    private TextView tv_nav_title;

    private LinearLayout ll_back;

    private NavOnClickListener navOnClickListener;

    private View contentView;

    private LinearLayout ll_nav;

    public Navigation(Context context) {
        this(context, null);
    }

    public Navigation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Navigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.navigation_layout, this);
        initContentView(contentView);
    }

    private void initContentView(View view) {
        nav_left = (LinearLayout) view.findViewById(R.id.ll_nav_left);
        nav_center = (LinearLayout) view.findViewById(R.id.ll_nav_center);
        nav_right = (LinearLayout) view.findViewById(R.id.ll_nav_right);
        tv_nav_title = (TextView) view.findViewById(R.id.tv_nav_title);
        ll_back = (LinearLayout) view.findViewById(R.id.ll_back);
        ll_nav = (LinearLayout) view.findViewById(R.id.ll_nav);
        ll_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navOnClickListener != null) {
                    navOnClickListener.onLeftClick(view);
                }
            }
        });
    }

    public void setNavleft(View view) {
        if (view == null) return;
        if (nav_left.getChildCount() > 0) {
            nav_left.removeAllViews();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
            parent.removeView(view);
        nav_left.addView(view);
    }

    public void setNavCenter(View view) {
        if (view == null) return;
        if (nav_center.getChildCount() > 0) {
            nav_center.removeAllViews();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
            parent.removeView(view);
        nav_center.addView(view);
    }

    public void setNavRight(View view) {
        if (view == null) return;
        if (nav_right.getChildCount() > 0) {
            nav_right.removeAllViews();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
            parent.removeView(view);
        nav_right.setGravity(Gravity.RIGHT);
        nav_right.setPadding(0, 0, dip2px(mContext, 15), 0);
        nav_right.addView(view);
    }

    /**
     * 将dp-->px
     *
     * @param context
     * @param dpValue
     * @return
     */
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface NavOnClickListener {
        void onLeftClick(View view);

        void onCenterClick(View view);

        void onRightClick(View view);
    }

    public void setTitle(String title) {
        if (title == null) return;
        tv_nav_title.setText(title);
    }

    public void setTitleColor(int color) {
        tv_nav_title.setTextColor(color);
    }

    public static class SimpleNavClickListenerImpl implements NavOnClickListener {

        @Override
        public void onLeftClick(View view) {

        }

        @Override
        public void onCenterClick(View view) {

        }

        @Override
        public void onRightClick(View view) {

        }
    }

    public void setNavOnClickListener(NavOnClickListener navOnClickListener) {
        this.navOnClickListener = navOnClickListener;
    }

    public void setNavBg(int color) {
        ll_nav.setBackgroundColor(color);
    }

    public LinearLayout getNav_left() {
        return nav_left;
    }

    public LinearLayout getNav_center() {
        return nav_center;
    }

    public LinearLayout getNav_right() {
        return nav_right;
    }
}
