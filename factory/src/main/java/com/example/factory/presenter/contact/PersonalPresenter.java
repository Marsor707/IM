package com.example.factory.presenter.contact;

import com.example.factory.Factory;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by marsor on 2017/7/4.
 */

public class PersonalPresenter extends BasePresenter<PersonalContract.View> implements PersonalContract.Presenter {
    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        //个人界面用户数据优先从网络拉取
        Factory.runOnAnsy(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view != null) {
                    String id = view.getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    onLoaded(user);
                }
            }
        });
    }

    /**
     * 进行界面的设置
     * @param user 用户的信息
     */
    private void onLoaded(final User user) {
        this.user = user;
        //是否是我自己
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        //是否关注了
        final boolean isFollow = isSelf || user.isFollow();
        //是否能聊天
        final boolean allowSayHello = user.isFollow() && !isSelf;

        //切换到ui线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                final PersonalContract.View view=getView();
                if(view==null)
                    return;
                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowSayHello(allowSayHello);
            }
        });
    }

    @Override
    public User getUserPersonal() {
        return user;
    }
}
