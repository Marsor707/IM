package com.example.marsor.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.common.app.Activity;
import com.example.common.widget.PortraitView;
import com.example.factory.persistence.Account;
import com.example.marsor.push.R;
import com.example.marsor.push.frags.main.ActiveFragment;
import com.example.marsor.push.frags.main.ContactFragment;
import com.example.marsor.push.frags.main.GroupFragment;
import com.example.marsor.push.helper.NavHelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {

    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    private NavHelper<Integer> mNavHelper;

    /**
     * MainActivity显示的入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (Account.isComplete()) {
            //判断用户信息是否完全，有则走正常流程
            return super.initArgs(bundle);
        } else {
            UserActivity.show(this);
            return false;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);
        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));
        mNavigation.setOnNavigationItemSelectedListener(this);
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        //接管Menu，手动触发第一次点击
        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home, 0);
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {
        //在群界面时 打开都为人搜索的界面
        int type=Objects.equals(mNavHelper.getCurrentTab().extra,R.string.title_group)?SearchActivity.TYPE_GROUP:SearchActivity.TYPE_USER;
        SearchActivity.show(this,type);
    }

    @OnClick(R.id.btn_action)
    void onActionClick() {
        //浮动按钮点击 判断当前界面是群还是人界面
        //如果是群 则打开群创建的界面
        if (Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group)) {
            //TODO 打开群的创建界面
        } else {
            // 如果是其他 都打开添加用户界面
            SearchActivity.show(this, SearchActivity.TYPE_USER);
        }
    }

    /**
     * 当底部导航栏点击时触发
     *
     * @param item MenuItem
     * @return true代表能处理这个点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //转接事件流到工具类当中
        return mNavHelper.performClickMenu(item.getItemId());
    }

    /**
     * NavHelper处理后回调的方法
     *
     * @param newTab 新的
     * @param oldTab 旧的
     */
    @Override
    public void OnTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        //从额外字段中取出title资源id
        mTitle.setText(newTab.extra);
        //对浮动按钮进行隐藏和显示的动画
        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            //主界面时隐藏
            transY = Ui.dipToPx(getResources(), 76);
        } else {
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                //群
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                //联系人
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }
        //开始动画
        //旋转，y轴位移，弹性差值器，时间
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();
    }
}
