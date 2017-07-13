package com.example.marsor.push.frags.message;


import android.support.v4.app.Fragment;

import com.example.factory.model.db.Group;
import com.example.factory.presenter.message.ChatContract;
import com.example.marsor.push.R;

/**
 * 群聊天界面的实现
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {


    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }
}
