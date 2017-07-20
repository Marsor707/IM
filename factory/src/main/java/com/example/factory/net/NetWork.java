package com.example.factory.net;

import android.text.TextUtils;

import com.example.common.Common;
import com.example.factory.Factory;
import com.example.factory.persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 * Created by marsor on 2017/5/29.
 */

public class NetWork {
    private static NetWork instance;
    private Retrofit retrofit;
    private OkHttpClient client;

    static {
        instance = new NetWork();
    }

    private NetWork() {

    }

    public static OkHttpClient getClient() {
        if (instance.client != null)
            return instance.client;
        //得到一个okhttp client

        instance.client = new OkHttpClient.Builder()
                //给所有的请求添加一个拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //拿到请求
                        Request original = chain.request();
                        //重新build
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            //注入一个token
                            builder.addHeader("token", Account.getToken());
                        }
                        builder.addHeader("Content-Type", "application/json");
                        Request request = builder.build();
                        //返回
                        return chain.proceed(request);
                    }
                })
                .build();
        return instance.client;
    }

    //构建一个Retrofit
    public static Retrofit getRetrofit() {
        if (instance.retrofit != null)
            return instance.retrofit;
        //得到一个okhttp client
        OkHttpClient client = getClient();

        Retrofit.Builder builder = new Retrofit.Builder();

        //设置电脑连接
        instance.retrofit = builder.baseUrl(Common.Constance.API_URL)
                //设置client
                .client(client)
                //设置json解析器
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
        return instance.retrofit;
    }

    /**
     * 返回一个请求的代理
     *
     * @return RemoteService
     */
    public static RemoteService remote() {
        //调用Retrofit对网络请求做代理
        return NetWork.getRetrofit().create(RemoteService.class);
    }
}
