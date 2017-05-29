package com.example.factory.data.helper;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.User;
import com.example.factory.net.NetWork;
import com.example.factory.net.RemoteService;
import com.example.factory.persistence.Account;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by marsor on 2017/5/28.
 */

public class AccountHelper {
    /**
     * 注册的接口 异步调用
     *
     * @param model    传递一个注册的model进来
     * @param callback 成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
        //调用Retrofit对网络请求做代理
        RemoteService service = NetWork.getRetrofit().create(RemoteService.class);
        //得到一个call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        //异步的请求
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
                //网络请求成功
                //从请求中得到全局Model,内部使用的是Gson解析
                RspModel<AccountRspModel> rspModel = response.body();
                if (rspModel.success()) {
                    //拿到实体
                    AccountRspModel accountRspModel = rspModel.getResult();
                    //获取我的信息
                    User user = accountRspModel.getUser();
                    //第一种 直接保存
                    user.save();

                        /*
                        //第二种 通过ModelAdapter保存
                        FlowManager.getModelAdapter(User.class)
                                .save(user);

                        //第三种 事物中
                        DatabaseDefinition definition=FlowManager.getDatabase(AppDatabase.class);
                        definition.beginTransactionAsync(new ITransaction() {
                            @Override
                            public void execute(DatabaseWrapper databaseWrapper) {
                                FlowManager.getModelAdapter(User.class)
                                        .save(user);
                            }
                        }).build().execute();
                        */
                    //同步到xml持久化中
                    Account.login(accountRspModel);

                    //判断设备绑定状态
                    if (accountRspModel.isBind()) {
                        //然后返回
                        callback.onDataLoaded(user);
                    } else {
                        //进行绑定
                        bindPush(callback);
                    }
                } else {
                    //错误解析
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                //网络请求失败
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 对设备id进行绑定的操作
     *
     * @param callback Callback
     */
    public static void bindPush(final DataSource.Callback<User> callback) {
        //TODO 先抛出一个错误 其实是绑定没有进行
        Account.setBind(true);
    }
}
