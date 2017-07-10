package com.example.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.util.DiffUtil;

import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.data.DataSource;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.user.ContactDataSource;
import com.example.factory.data.user.ContactRepository;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BasePresenter;
import com.example.factory.presenter.BaseRecyclerPresenter;
import com.example.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人的Presenter的实现
 * Created by marsor on 2017/7/3.
 */

public class ContactPresenter extends BaseRecyclerPresenter<User,ContactContract.View> implements ContactContract.Presenter,DataSource.SucceedCallback<List<User>> {
    private ContactDataSource mSource;

    public ContactPresenter(ContactContract.View view) {
        super(view);
        mSource=new ContactRepository();
    }

    @Override
    public void start() {
        super.start();
        mSource.load(this);

        //加载网络数据
        UserHelper.refreshContacts();


    }

    private void diff(List<User> oldList, List<User> newList) {
        //进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldList, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        //在对比完成后进行数据的赋值
        getView().getRecyclerAdapter().replace(newList);
        //尝试刷新界面
        result.dispatchUpdatesTo(getView().getRecyclerAdapter());
        getView().onAdapterDataChanged();
    }

    //运行到这里的时候是子线程
    @Override
    public void onDataLoaded(List<User> users) {
        //无论怎么操作 数据变更都会通知到这里来
        final ContactContract.View view=getView();
        if(view==null)
            return;
        RecyclerAdapter<User> adapter=view.getRecyclerAdapter();
        List<User> old=adapter.getItems();

        //进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //调用基类方法进行界面刷新
        refreshData(result,users);

    }

    @Override
    public void destroy() {
        super.destroy();
        //当界面销毁时 把数据监听销毁
        mSource.dispose();
    }
}
