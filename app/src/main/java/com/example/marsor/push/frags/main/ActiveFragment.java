package com.example.marsor.push.frags.main;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.common.app.PresenterFragment;
import com.example.common.widget.EmptyView;
import com.example.common.widget.GalleryView;
import com.example.common.widget.PortraitView;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.face.Face;
import com.example.factory.model.db.Session;
import com.example.factory.model.db.User;
import com.example.factory.presenter.message.SessionContract;
import com.example.factory.presenter.message.SessionPresenter;
import com.example.marsor.push.R;
import com.example.marsor.push.activities.MessageActivity;
import com.example.marsor.push.activities.PersonalActivity;
import com.example.utils.DateTimeUtil;

import net.qiujuer.genius.ui.Ui;

import butterknife.BindView;
import butterknife.OnClick;

public class ActiveFragment extends PresenterFragment<SessionContract.Presenter> implements SessionContract.View {
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    //Adapter,User,可以直接从数据库查询数据
    private RecyclerAdapter<Session> mAdapter;

    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        //初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {
                //返回cell布局的id
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Session session) {
                //跳转到聊天界面
                MessageActivity.show(getContext(), session);
            }
        });
        //初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        //进行一次数据加载
        mPresenter.start();
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_content)
        TextView mContent;

        @BindView(R.id.txt_time)
        TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {
            mPortraitView.setup(Glide.with(ActiveFragment.this), session.getPicture());
            mName.setText(session.getTitle());

            String str = TextUtils.isEmpty(session.getContent()) ? "" : session.getContent();
            Spannable spannable = new SpannableString(str);
            //解析表情
            Face.decode(mContent, spannable, (int) mContent.getTextSize());
            //把文字设置到布局上
            mContent.setText(spannable);

            mTime.setText(DateTimeUtil.getSimpleDate(session.getModifyAt()));
        }
    }
}
