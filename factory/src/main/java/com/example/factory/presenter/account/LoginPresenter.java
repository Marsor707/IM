package com.example.factory.presenter.account;

import com.example.factory.presenter.BasePresenter;

/**
 * 登陆的逻辑实现
 * Created by marsor on 2017/5/29.
 */

public class LoginPresenter extends BasePresenter<LoginContact.View> implements LoginContact.Presenter {
    public LoginPresenter(LoginContact.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

    }
}
