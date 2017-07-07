package com.example.factory.data.group;

import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;

/**
 * Created by marsor on 2017/7/7.
 */

public interface GroupCenter {
    //群卡片的处理
    void dispatch(GroupCard... cards);

    //群成员的处理
    void dispatch(GroupMemberCard... cards);
}
