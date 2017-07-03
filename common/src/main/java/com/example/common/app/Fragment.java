package com.example.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by marsor on 2017/5/15.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnBinder;
    protected PlaceHolderView mPlaceHolderView;
    //标示是否第一次初始化数据
    protected boolean mIsFirstInitData=true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRoot==null) {
            int layId = getContentLayoutId();
            //初始化当前根布局，但是不再创建是就添加进container中去
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        }else {
            if(mRoot.getParent()!=null){
                //把当前root从其父控件中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mIsFirstInitData==true){
            //触发一次后就不会触发
            mIsFirstInitData=false;
            onFirstInit();
        }
        //当view创建完后初始化数据
        initData();
    }

    /**
     * 初始化相关参数
     * @param bundle 参数Bundle
     */
    protected void initArgs(Bundle bundle){
    }

    /**
     * 得到当前界面的资源文件ID
     * 子类必须复写
     * @return 资源文件ID
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View root){
        mRootUnBinder=ButterKnife.bind(this,root);
    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    /**
     * 当首次初始化数据时会调用
     */
    protected void onFirstInit(){

    }

    /**
     * 返回按键触发时调用
     * @return 返回true代表我已处理逻辑，Activity不用自己finish
     * 返回false代表我没有处理逻辑，Activity走自己的逻辑
     */
    public boolean onBackPressed(){
        return false;
    }

    /**
     * 设置占位布局
     * @param placeHolderView 继承了PlaceHolderView规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView){
        this.mPlaceHolderView=placeHolderView;
    }
}
