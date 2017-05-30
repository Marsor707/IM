package com.example.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 登陆的逻辑实现
 * Created by marsor on 2017/5/29.
 */

public class LoginPresenter extends BasePresenter<LoginContact.View> implements LoginContact.Presenter, DataSource.Callback<User> {
    public LoginPresenter(LoginContact.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();
        final LoginContact.View view = getView();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            //尝试传递pushId
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(model, this);
        }
    }

    @Override
    public void onDataLoaded(User user) {
        final LoginContact.View view = getView();
        if (view == null)
            return;
        //强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final LoginContact.View view = getView();
        if (view == null)
            return;
        //此时是网络回送回来的，并不保证是在主线程
        //强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
