package com.example.marsor.push.activities;

import android.content.Intent;

import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.marsor.push.R;
import com.example.marsor.push.frags.user.UpdateInfoFragment;

public class UserActivity extends Activity {
    private Fragment mCurFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
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
