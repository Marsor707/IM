package com.example.factory.data.user;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.data.DataSource;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * 联系人的数据仓库
 * Created by marsor on 2017/7/9.
 */

public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource {
    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        super.load(callback);
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this).execute();
    }

    @Override
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }
}
