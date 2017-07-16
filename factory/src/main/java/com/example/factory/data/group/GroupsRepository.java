package com.example.factory.data.group;

import android.text.TextUtils;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Group_Table;
import com.example.factory.model.db.view.MemberUserModel;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * 我们群组的数据仓库 是对GroupsDataSource 的实现
 * Created by marsor on 2017/7/16.
 */

public class GroupsRepository extends BaseDbRepository<Group> implements GroupsDataSource {

    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Group group) {
        if (group.getGroupMemberCount() > 0) {
            group.holder = buildGroupHolder(group);
        } else {
            group.holder = null;
            GroupHelper.refreshGroupMember(group);
        }

        //所有的群都关注
        return true;
    }

    //初始化界面显示的成员信息
    private String buildGroupHolder(Group group) {
        List<MemberUserModel> userModels = group.getLatelyMembers();
        if (userModels == null || userModels.size() == 0)
            return null;
        StringBuilder builder = new StringBuilder();
        for (MemberUserModel userModel : userModels) {
            builder.append(TextUtils.isEmpty(userModel.alias) ? userModel.name : userModel.alias);
            builder.append(", ");
        }
        builder.delete(builder.lastIndexOf(", "), builder.length());
        return builder.toString();
    }
}
