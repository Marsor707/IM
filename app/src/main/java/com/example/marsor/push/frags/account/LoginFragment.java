package com.example.marsor.push.frags.account;


import android.content.Context;

import com.example.common.app.Fragment;
import com.example.marsor.push.R;

/**
 * 登陆的界面
 */
public class LoginFragment extends Fragment {
    private AccountTrigger mAccountTrigger;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到Activity的引用
        mAccountTrigger= (AccountTrigger) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onResume() {
        super.onResume();
        //进行一次切换，默认切换到注册界面
        mAccountTrigger.triggerView();
    }
}
