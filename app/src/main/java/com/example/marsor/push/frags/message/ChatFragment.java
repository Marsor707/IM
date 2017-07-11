package com.example.marsor.push.frags.message;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.common.app.Fragment;
import com.example.common.widget.adapter.TextWatcherAdapter;
import com.example.marsor.push.R;
import com.example.marsor.push.activities.MessageActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by marsor on 2017/7/11.
 */

public abstract class ChatFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    protected String mReceiverId;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.edit_content)
    EditText mContent;

    @BindView(R.id.btn_submit)
    View mSubmit;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initToolbar();
        initAppbar();
        initEditContent();
        //RecyclerView基本设置
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    //初始化Toolbar
    protected void initToolbar() {
        Toolbar toolbar = mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    //给界面appbar设置一个监听 的到关闭与打开时的进度
    private void initAppbar() {
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    //初始化输入框监听
    private void initEditContent() {
        mContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                //设置状态 改变对应的icon
                mSubmit.setActivated(needSendMsg);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @OnClick(R.id.btn_face)
    void onFaceClick() {
        //TODO
    }

    @OnClick(R.id.btn_record)
    void onRecordClick() {
        //TODO
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mSubmit.isActivated()) {
            //发送
        } else {
            onMoreClick();
        }
    }

    private void onMoreClick() {
        //TODO
    }
}
