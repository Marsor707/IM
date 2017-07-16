package com.example.factory.model.db.view;

import com.example.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

/**
 * 群成员对应的用户的简单信息表
 * Created by marsor on 2017/7/16.
 */

@QueryModel(database = AppDatabase.class)
public class MemberUserModel {
    @Column
    public String id;
    @Column
    public String name;
    @Column
    public String alias;
    @Column
    public String portrait;


}
