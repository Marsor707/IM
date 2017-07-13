package com.example.factory.data.message;

import com.example.factory.data.DbDataSource;
import com.example.factory.model.db.Message;

/**
 * 消息的数据源定义 他的实现是MessageRepository
 * 关注的对象是Message表
 * Created by marsor on 2017/7/12.
 */

public interface MessageDataSource extends DbDataSource<Message>{
}
