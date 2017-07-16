package com.example.factory.data.helper;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.group.GroupCreateModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Group_Table;
import com.example.factory.model.db.User;
import com.example.factory.net.NetWork;
import com.example.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 对群的一个简单的辅助工具类
 * Created by marsor on 2017/7/7.
 */

public class GroupHelper {
    public static Group find(String groupId) {
        Group group = findFromLocal(groupId);
        if (group == null)
            group = findFromNet(groupId);
        return group;
    }

    public static Group findFromLocal(String groupId) {
        return SQLite.select()
                .from(Group.class)
                .where(Group_Table.id.eq(groupId))
                .querySingle();
    }

    public static Group findFromNet(String id) {
        RemoteService remoteService = NetWork.remote();
        try {
            Response<RspModel<GroupCard>> response = remoteService.groupFind(id).execute();
            GroupCard card = response.body().getResult();
            if (card != null) {
                //数据库的存储并通知
                Factory.getGroupCenter().dispatch(card);
                User user = UserHelper.search(card.getOwnerId());
                if (user != null) {
                    return card.build(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //群的创建
    public static void create(GroupCreateModel model, final DataSource.Callback<GroupCard> callback) {
        RemoteService service = NetWork.remote();
        service.groupCreate(model)
                .enqueue(new Callback<RspModel<GroupCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<GroupCard>> call, Response<RspModel<GroupCard>> response) {
                        RspModel<GroupCard> rspModel = response.body();
                        if (rspModel.success()) {
                            GroupCard groupCard = rspModel.getResult();
                            Factory.getGroupCenter().dispatch(groupCard);
                            callback.onDataLoaded(groupCard);
                        } else {
                            Factory.decodeRspCode(rspModel, callback);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<GroupCard>> call, Throwable t) {
                        callback.onDataNotAvailable(R.string.data_network_error);
                    }
                });
    }
}
