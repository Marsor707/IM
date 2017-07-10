package com.example.marsor.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.persistence.Account;
import com.igexin.sdk.PushConsts;

/**
 * 个推的消息接收器
 * Created by marsor on 2017/5/29.
 */

public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;
        Bundle bundle = intent.getExtras();
        //判断当前消息的意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID: {
                Log.i(TAG, "GET_CLIENTID:" + bundle.toString());
                //当id初始化的时候
                //获取设备id
                onClientInit(bundle.getString("clientid"));
                break;
            }
            case PushConsts.GET_MSG_DATA: {
                //常规消息送达
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String message = new String(payload);
                    Log.i(TAG, "GET_MSG_DATA:" + message);
                    onMessageArrived(message);
                    break;
                }
            }
            default:
                Log.i(TAG, "OTHER:" + bundle.toString());
                break;
        }
    }

    /**
     * 当id初始化的时候
     *
     * @param cid 设备id
     */
    private void onClientInit(String cid) {
        Account.setPushId(cid);
        if(Account.isLogin()){
            //账户登录 进行一次pushId绑定 没有登录情况下不能绑定pushId
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息到达时
     *
     * @param message 新消息
     */
    private void onMessageArrived(String message) {
        //交给Factory处理
        Factory.dispatchPush(message);
    }
}
