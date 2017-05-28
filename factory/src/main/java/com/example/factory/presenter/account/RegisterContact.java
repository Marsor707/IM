package com.example.factory.presenter.account;

import com.example.factory.presenter.BaseContract;

/**
 * Created by marsor on 2017/5/28.
 */

public interface RegisterContact {
    interface View extends BaseContract.View<Presenter>{
        //注册成功
        void registerSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        //发起一个注册
        void register(String phone,String name,String password);

        //检查手机号是否正确
        boolean CheckMobile(String phone);
    }
}
