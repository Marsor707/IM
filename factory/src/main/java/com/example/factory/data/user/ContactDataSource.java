package com.example.factory.data.user;

import com.example.factory.data.DataSource;
import com.example.factory.model.db.User;

import java.util.List;

/**
 * 联系人数据源
 * Created by marsor on 2017/7/9.
 */

public interface ContactDataSource {
    /**
     * 对数据进行加载的一个职责
     *
     * @param callback 加载成功后返回的callback
     */
    void load(DataSource.SucceedCallback<List<User>> callback);

    /**
     * 销毁操作
     */
    void dispose();
}
