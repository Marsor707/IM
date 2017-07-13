package com.example.factory.presenter.message;

import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.message.MessageRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

/**
 * Created by marsor on 2017/7/12.
 */

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView> implements ChatContract.Presenter {

    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        //数据源 view 接收者 接收者的类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();
        //从本地拿到这个人的信息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(receiver);
    }
}
