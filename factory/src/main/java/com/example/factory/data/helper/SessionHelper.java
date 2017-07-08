package com.example.factory.data.helper;

import com.example.factory.model.db.Session;
import com.example.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * 会话辅助工具类
 * Created by marsor on 2017/7/8.
 */

public class SessionHelper {
    public static Session findFromLocal(String id) {
        //从本地查询session
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
