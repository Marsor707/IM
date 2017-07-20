package com.example.marsor.push;

import android.content.Context;

import com.example.common.app.Application;
import com.example.factory.Factory;
import com.example.marsor.push.activities.AccountActivity;
import com.igexin.sdk.PushManager;

import java.io.File;

/**
 * Created by marsor on 2017/5/20.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //调用Factory进行初始化
        Factory.setup();
        //推送进行初始化
        PushManager.getInstance().initialize(this);
    }

    @Override
    protected void showAccountView(Context context) {
        //登录界面显示
        AccountActivity.show(context);
        //cleanDatabase(context);
    }

    private void cleanDatabase(Context context) {
        File databaseDir = new File("/data/data/" + context.getPackageName() + "/databases");
        if (databaseDir != null && databaseDir.exists() && databaseDir.isDirectory()) {
            for (File file : databaseDir.listFiles()) {
                file.delete();
            }
        }
    }
}
