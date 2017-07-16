package com.example.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import com.example.factory.data.group.GroupsDataSource;
import com.example.factory.data.group.GroupsRepository;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.Group;
import com.example.factory.presenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 我们群组的Presenter
 * Created by marsor on 2017/7/16.
 */

public class GroupsPresenter extends BaseSourcePresenter<Group, Group, GroupsDataSource, GroupsContract.View> implements GroupsContract.Presenter {
    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        //加载网络数据 以后可以优化到下拉刷新中
        //只有用户下拉进行网络请求
        GroupHelper.refreshGroups();
    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        GroupsContract.View view = getView();
        if (view == null)
            return;
        List<Group> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, groups);
    }
}
