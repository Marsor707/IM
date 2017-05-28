package com.example.common.app;

import android.content.Context;
import android.support.annotation.StringRes;

import com.example.factory.presenter.BaseContract;

/**
 * Created by marsor on 2017/5/28.
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //在界面onAttach后就触发初始化Presenter
        initPresenter();
    }

    /**
     * 初始化Presenter
     * @return Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showError(@StringRes int str) {
        //显示错误
        Application.showToast(str);
    }

    @Override
    public void showLoading() {
        //TODO 显示一个loading
    }

    @Override
    public void setPresenter(Presenter presenter) {
        //View中赋值Presenter
        mPresenter = presenter;
    }
}
