package com.example.factory.data;

import java.util.List;

/**
 * 基础的数据库数据源接口定义
 * Created by marsor on 2017/7/10.
 */

public interface DbDataSource<Data> extends DataSource {
    /**
     * 有一个基本的数据加载方法
     *
     * @param callback 传递一个callback回调 一般回调到Presenter
     */
    void load(SucceedCallback<List<Data>> callback);

}
