package com.example.factory.presenter.group;

import com.example.factory.model.db.view.MemberUserModel;
import com.example.factory.presenter.BaseContract;

/**
 * 群成员的契约
 * Created by marsor on 2017/7/17.
 */

public interface GroupMembersContract {
    interface Presenter extends BaseContract.Presenter{
        void refresh();
    }

    interface View extends BaseContract.RecyclerView<Presenter,MemberUserModel>{
        String getGroupId();
    }
}
