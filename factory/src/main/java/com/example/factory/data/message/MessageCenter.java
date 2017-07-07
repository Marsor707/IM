package com.example.factory.data.message;

import com.example.factory.model.card.MessageCard;

/**
 * 消息中心 进行消息卡片的消费
 * Created by marsor on 2017/7/7.
 */

public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
