package com.example.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.util.DiffUtil;

import com.example.factory.data.DataSource;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BasePresenter;
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

public class ContactPresenter extends BasePresenter<ContactContract.View> implements ContactContract.Presenter {
    public ContactPresenter(ContactContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        //加载本地数据库数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        getView().getRecyclerAdapter().replace(tResult);
                        getView().onAdapterDataChanged();
                    }
                }).execute();

        UserHelper.refreshContacts(new DataSource.Callback<List<UserCard>>() {
            @Override
            public void onDataNotAvailable(@StringRes int strRes) {
                //网络失败 因为本地有数据 所以不处理
            }

            @Override
            public void onDataLoaded(final List<UserCard> userCards) {
                final List<User> users = new ArrayList<User>();
                for (UserCard userCard : userCards) {
                    users.add(userCard.build());
                }

                //丢到事务中保存数据库
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        for (UserCard userCard : userCards) {
                            FlowManager.getModelAdapter(User.class)
                                    .saveAll(users);
                        }
                    }
                }).build().execute();

                //网络数据往往是新的 需要刷新到界面
                List<User> old = getView().getRecyclerAdapter().getItems();
                //会导致数据顺序全部为新的数据集合
                //getView().getRecyclerAdapter().replace(users);
                diff(old, users);
            }
        });
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
}
