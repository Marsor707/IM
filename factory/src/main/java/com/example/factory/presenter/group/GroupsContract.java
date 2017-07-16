package com.example.factory.presenter.group;

import com.example.factory.model.db.Group;
import com.example.factory.model.db.User;
import com.example.factory.presenter.BaseContract;

/**
 * 我的群列表契约
 * Created by marsor on 2017/7/3.
 */

public interface GroupsContract {
    //什么都不需要做 开始就是调用start
    interface Presenter extends BaseContract.Presenter{

    }

    //都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter,Group>{

    }
}
