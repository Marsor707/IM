package com.example.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.example.common.Common;
import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.db.User;
import com.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/**
 * Created by marsor on 2017/5/28.
 */

public class RegisterPresenter extends BasePresenter<RegisterContact.View> implements RegisterContact.Presenter ,DataSource.Callback<User>{
    public RegisterPresenter(RegisterContact.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        //调用开始方法 在start中默认启动了loading
        start();

        //得到View接口
        RegisterContact.View view=getView();

        //校验
        if(!CheckMobile(phone)){
            //提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        }else if(name.length()<2){
            //姓名需要大于2位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        }else if(password.length()<6){
            //密码需要大于6位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        }else {
            //进行网络请求
            //构造Model进行请求调用
            RegisterModel model=new RegisterModel(phone,password,name);
            //进行网络请求并设置回送接口为自己
            AccountHelper.register(model,this);
        }
    }

    /**
     * 检查手机号是否合法
     * @param phone 手机号
     * @return 合法为True
     */
    @Override
    public boolean CheckMobile(String phone) {
        //手机号不为空 并满足相应格式
        return !TextUtils.isEmpty(phone)&& Pattern.matches(Common.Constance.REGEX_MOBILE,phone);
    }

    @Override
    public void onDataLoaded(User user) {
        //网络请求成功 注册好了 回送一个用户信息
        //告知界面，注册成功
        final RegisterContact.View view=getView();
        if(view==null)
            return;
        //此时是网络回送回来的，并不保证是在主线程
        //强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //调用界面注册成功
                view.registerSuccess();
            }
        });

    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        //网络请求告知注册失败
        final RegisterContact.View view=getView();
        if(view==null)
            return;
        //此时是网络回送回来的，并不保证是在主线程
        //强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //调用界面注册失败 显示错误
                view.showError(strRes);
            }
        });
    }
}
