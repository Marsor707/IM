package com.example.factory.presenter;

/**
 * Created by marsor on 2017/5/28.
 */

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {
    private T mView;

    public BasePresenter(T view) {
        setView(view);
    }

    /**
     * 设置一个View 子类可以复写
     */
    @SuppressWarnings("unchecked")
    protected void setView(T view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    /**
     * 给子类使用的获取view的操作 不可以复写
     *
     * @return View
     */
    protected final T getView() {
        return mView;
    }

    @Override
    public void start() {
        //开始的时候进行loading调用
        T view = mView;
        if (view != null) {
            view.showLoading();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void destroy() {
        T view = mView;
        mView = null;
        if (view != null) {
            //把Presenter设置为null
            view.setPresenter(null);
        }
    }
}
