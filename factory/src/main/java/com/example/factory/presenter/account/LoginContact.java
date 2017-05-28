package com.example.factory.presenter.account;

import com.example.factory.presenter.BaseContract;

/**
 * Created by marsor on 2017/5/28.
 */

public interface LoginContact {
    interface View extends BaseContract.View<Presenter>{
        //登陆成功
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        //发起一个登陆
        void login(String phone, String name, String password);
    }
}
