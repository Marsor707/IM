package com.example.factory.presenter.contact;

import com.example.factory.model.db.User;
import com.example.factory.presenter.BaseContract;

/**
 * Created by marsor on 2017/7/3.
 */

public interface ContactContract {
    //什么都不需要做 开始就是调用start
    interface Presenter extends BaseContract.Presenter{

    }

    //都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter,User>{

    }
}
