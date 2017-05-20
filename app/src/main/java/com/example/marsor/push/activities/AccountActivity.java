package com.example.marsor.push.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.marsor.push.R;
import com.example.marsor.push.frags.account.UpdateInfoFragment;
import com.yalantis.ucrop.UCrop;

public class AccountActivity extends Activity {
    private Fragment mCurFragment;

    /**
     * 账户Activity显示的入口
     * @param context Context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment=new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,mCurFragment)
                .commit();
    }

    //Activity中收到图片成功剪切的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode,resultCode,data);
    }
}
