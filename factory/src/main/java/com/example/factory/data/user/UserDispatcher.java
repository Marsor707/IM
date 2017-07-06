package com.example.factory.data.user;

import com.example.factory.model.card.UserCard;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by marsor on 2017/7/6.
 */

public class UserDispatcher implements UserCenter {
    private UserCenter instance;
    //单线程池 处理卡片一个一个的消息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void dispatch(UserCard... cards) {

    }
}
