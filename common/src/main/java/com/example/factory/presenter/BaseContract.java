package com.example.factory.presenter;

import android.support.annotation.StringRes;

import com.example.common.widget.recycler.RecyclerAdapter;

/**
 * MVP模式中公共的基本契约
 * Created by marsor on 2017/5/28.
 */

public interface BaseContract {
    //基本的界面职责
    interface View<T extends Presenter>{
        //公共的显示一个字符串错误
        void showError(@StringRes int str);

        //公共的显示进度条
        void showLoading();

        //支持设置Presenter
        void setPresenter(T presenter);
    }

    //基本的Presenter职责
    interface Presenter{
        //公用的开始触发
        void start();

        //公用的销毁触发
        void destroy();
    }

    //基本的一个列表的View的职责
    interface RecyclerView<T extends Presenter,ViewMode> extends View<T>{
        //界面端只能刷新整个数据集合，不能精确到每一条数据更新
        //void onDone(List<User> users);

        //拿到适配器 然后自主刷新
        RecyclerAdapter<ViewMode> getRecyclerAdapter();

        //当适配器数据更改了的时候触发
        void onAdapterDataChanged();
    }
}
