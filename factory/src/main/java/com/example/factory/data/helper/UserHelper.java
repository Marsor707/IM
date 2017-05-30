package com.example.factory.data.helper;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.net.NetWork;
import com.example.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by marsor on 2017/5/30.
 */

public class UserHelper {
    //更新用户信息的操作 异步
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        //调用Retrofit对网络请求做代理
        RemoteService service = NetWork.remote();
        //得到一个call
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        //网络请求
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    //数据库的操作 需要把UserCard转换为User
                    //保存用户信息
                    User user=userCard.build();
                    user.save();
                    //返回成功
                    callback.onDataLoaded(userCard);
                } else {
                    //错误情况下进行错误分配
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
