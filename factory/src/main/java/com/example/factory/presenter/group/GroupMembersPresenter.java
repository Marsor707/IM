package com.example.factory.presenter.group;

import com.example.factory.Factory;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.view.MemberUserModel;
import com.example.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * Created by marsor on 2017/7/17.
 */

public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMembersContract.View> implements GroupMembersContract.Presenter {
    public GroupMembersPresenter(GroupMembersContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        start();
        Factory.runOnAnsy(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMembersContract.View view = getView();
            if (view == null)
                return;
            String groupId = view.getGroupId();

            //传递数量为-1代表查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);

            refreshData(models);
        }
    };
}
