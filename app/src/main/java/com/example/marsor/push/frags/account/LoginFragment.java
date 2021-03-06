package com.example.marsor.push.frags.account;


import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.common.app.PresenterFragment;
import com.example.factory.presenter.account.LoginContact;
import com.example.factory.presenter.account.LoginPresenter;
import com.example.marsor.push.R;
import com.example.marsor.push.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登陆的界面
 */
public class LoginFragment extends PresenterFragment<LoginContact.Presenter> implements LoginContact.View {
    private AccountTrigger mAccountTrigger;

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;

    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.txt_go_register)
    TextView mGoRegister;

    @BindView(R.id.btn_submit)
    Button mSubmit;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到Activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected LoginContact.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        //调用P层进行注册
        mPresenter.login(phone, password);
    }

    @OnClick(R.id.txt_go_register)
    void onShowRegisterClick() {
        //让AccountActivity进行界面切换
        mAccountTrigger.triggerView();
    }

    @Override
    public void showError(@StringRes int str) {
        super.showError(str);
        //当需要显示错误时触发，一定是结束了
        //停止loading
        mLoading.stop();
        //让控件可以输入
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mGoRegister.setEnabled(true);
        //提交按钮可以继续点击
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        //正在进行时 正在注册 界面不可操作
        //开始loading
        mLoading.start();
        //让控件不可以输入
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mGoRegister.setEnabled(false);
        //提交按钮不可以继续点击
        mSubmit.setEnabled(false);
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
