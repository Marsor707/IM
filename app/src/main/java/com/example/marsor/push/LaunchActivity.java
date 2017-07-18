package com.example.marsor.push;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import com.example.common.app.Activity;
import com.example.factory.persistence.Account;
import com.example.marsor.push.activities.AccountActivity;
import com.example.marsor.push.activities.MainActivity;
import com.example.marsor.push.frags.assist.PermissionsFragment;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

public class LaunchActivity extends Activity implements PermissionsFragment.Callback{
    //Drawable
    private ColorDrawable mBgDrawable;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //拿到根布局
        View root = findViewById(R.id.activity_launch);
        //获取颜色
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        //创建一个Drawable
        ColorDrawable drawable = new ColorDrawable(color);
        //设置给背景
        root.setBackground(drawable);
        mBgDrawable = drawable;
    }

    @Override
    protected void initData() {
        super.initData();
        //动画进行到50%等待pushId得到
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                //检测等待状态
                waitPushReceiverId();
            }
        });
    }

    /**
     * 等待个推框架对pushId设置好值
     */
    private void waitPushReceiverId() {
        if (Account.isLogin()) {
            //已经登录判断是否已经绑定
            //如果没有绑定则等待广播接收器进行绑定
            if (Account.isBind()) {
                skip();
                return;
            }
        } else {
            //没有登录
            //如果拿到了pushId 没有登录情况下不能绑定pushId
            if (!TextUtils.isEmpty(Account.getPushId())) {
                skip();
                return;
            }
        }

        //循环等待
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        }, 500);
    }

    /**
     * 在跳转以前需要将剩下的50%进行完成
     */
    private void skip() {
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });
    }

    /**
     * 真实的跳转
     */
    private void reallySkip() {
        //权限检测 跳转
        if(PermissionsFragment.haveAll(this, getSupportFragmentManager()))
            login();
    }

    /**
     * 给背景设置一个动画
     *
     * @param endProgress 动画结束进度
     * @param endCallback 结束时触发
     */
    private void startAnim(float endProgress, final Runnable endCallback) {
        //获取一个最终颜色
        int finalColor = Resource.Color.WHITE;
        //计算当前进度的颜色
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = (int) evaluator.evaluate(endProgress, mBgDrawable.getColor(), finalColor);
        //构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, evaluator, endColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mBgDrawable.getColor(), endColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //结束时触发
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    private final Property<LaunchActivity, Object> property = new Property<LaunchActivity, Object>(Object.class, "color") {
        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }

        @Override
        public Object get(LaunchActivity object) {
            return object.mBgDrawable.getColor();
        }
    };

    @Override
    public void login() {
        //检查跳转到主页还是登录
        if (Account.isLogin()) {
            MainActivity.show(this);
        } else {
            AccountActivity.show(this);
        }
        finish();
    }
}
