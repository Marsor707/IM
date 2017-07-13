package com.example.factory.data.helper;

import com.example.factory.Factory;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.example.factory.model.db.Session;
import com.example.factory.model.db.Session_Table;
import com.example.factory.net.NetWork;
import com.example.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息工具类
 * Created by marsor on 2017/7/7.
 */

public class MessageHelper {
    // 从本地找消息
    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    //发送是异步进行的
    public static void push(final MsgCreateModel model) {
        Factory.runOnAnsy(new Runnable() {
            @Override
            public void run() {
                //成功状态：如果是一个已发送过的消息 则不能重新发送
                //正在发送状态：如果一个消息正在发送 则不能重新发送
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED)
                    return;

                //TODO 如果是文件类型的（语言 图片 文件）需先上传后再发送

                //我们在发送的时候需要通知界面更新状态 card
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);

                //直接发送进行网络调度
                RemoteService service = NetWork.remote();
                service.msgPush(model).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (call != null) {
                                //成功的调度
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            //检查账户是否异常
                            Factory.decodeRspCode(rspModel, null);
                            //走失败流程
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        //通知失败
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }

    /**
     * 查询一个消息 这个消息是一个群的最后一条消息
     *
     * @param groupId 群id
     * @return 群聊天中的最后一条消息
     */
    public static Message findLastWithGroup(String groupId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }

    /**
     * 查询一个消息 这个消息是一个人的最后一条消息
     * @param userId userId
     * @return Message
     */
    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }
}
