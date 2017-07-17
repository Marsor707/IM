package com.example.factory.presenter.message;

import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.message.MessageGroupRepository;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.view.MemberUserModel;
import com.example.factory.persistence.Account;

import java.util.List;

/**
 * Created by marsor on 2017/7/12.
 */

public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView> implements ChatContract.Presenter {

    public ChatGroupPresenter(ChatContract.GroupView view, String receiverId) {
        //数据源 view 接收者 接收者的类型
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();
        //那群的信息
        Group group = GroupHelper.findFromLocal(mReceiverId);
        if (group != null) {
            //初始化操作
            ChatContract.GroupView view = getView();

            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
            view.showAdminOption(isAdmin);
            view.onInit(group);
            List<MemberUserModel> models = group.getLatelyMembers();
            final long memberCount = group.getGroupMemberCount();
            long moreCount = memberCount - models.size();
            view.onInitGroupMembers(models, moreCount);
        }
    }
}
