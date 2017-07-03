package com.example.factory.presenter.contact;

import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.BaseContract;

/**
 * 关注的接口定义
 * Created by marsor on 2017/7/3.
 */

public interface FollowContract {
    //任务调度者
    interface Presenter extends BaseContract.Presenter{
        //关注
        void follow(String id);
    }

    interface View extends BaseContract.View<Presenter>{
        //成功情况下返回一个用户信息
        void onFollowSucceed(UserCard userCard);
    }
}
