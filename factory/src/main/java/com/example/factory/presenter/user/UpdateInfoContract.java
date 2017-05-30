package com.example.factory.presenter.user;

import com.example.factory.presenter.BaseContract;

/**
 * 更新用户信息的基本契约
 * Created by marsor on 2017/5/30.
 */

public interface UpdateInfoContract {
    interface Presenter extends BaseContract.Presenter {
        //更新
        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {
        //回调成功
        void updateSucceed();
    }
}
