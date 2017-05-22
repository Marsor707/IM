package com.example.marsor.push;

import com.example.common.app.Activity;
import com.example.marsor.push.activities.MainActivity;
import com.example.marsor.push.frags.assist.PermissionsFragment;

public class LaunchActivity extends Activity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PermissionsFragment.haveAll(this,getSupportFragmentManager())){
            MainActivity.show(this);
            finish();
        }
    }
}
