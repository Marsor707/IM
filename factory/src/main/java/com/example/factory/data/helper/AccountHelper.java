package com.example.factory.data.helper;

import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.db.User;

/**
 * Created by marsor on 2017/5/28.
 */

public class AccountHelper {
    /**
     * 注册的接口 异步调用
     * @param model 传递一个注册的model进来
     * @param callback 成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callback.onDataNotAvailable(R.string.data_rsp_error_parameters);
            }
        }.start();
    }
}
