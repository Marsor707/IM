package com.example.factory.net;

import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 网络请求的所有接口
 * Created by marsor on 2017/5/29.
 */

public interface RemoteService {
    /**
     * 网络请求 一个注册接口
     *
     * @param model RegisterModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     *
     * @param model LoginModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备id
     *
     * @param pushId 设备id
     * @return RspModel<AccountRspModel>
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);
}
