package com.example.marsor.push.frags.message;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.app.Application;
import com.example.common.app.PresenterFragment;
import com.example.common.tools.AudioPlayHelper;
import com.example.common.widget.PortraitView;
import com.example.common.widget.adapter.TextWatcherAdapter;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.face.Face;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.message.ChatContract;
import com.example.factory.utils.FileCache;
import com.example.marsor.push.R;
import com.example.marsor.push.activities.MessageActivity;
import com.example.marsor.push.frags.panel.PanelFragment;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.widget.airpanel.AirPanel;
import net.qiujuer.widget.airpanel.Util;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by marsor on 2017/7/11.
 */

public abstract class ChatFragment<InitModel>
        extends PresenterFragment<ChatContract.Presenter>
        implements AppBarLayout.OnOffsetChangedListener, ChatContract.View<InitModel>, PanelFragment.PanelCallback {
    protected String mReceiverId;
    protected Adapter mAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingLayout;

    @BindView(R.id.edit_content)
    EditText mContent;

    @BindView(R.id.btn_submit)
    View mSubmit;

    private AirPanel.Boss mPanelBoss;
    private PanelFragment mPanelFragment;
    //语音的基础
    private FileCache<AudiHolder> mAudioFileCache;
    private AudioPlayHelper<AudiHolder> mAudioPlayer;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected final int getContentLayoutId() {
        return R.layout.fragment_chat_common;
    }

    @LayoutRes
    protected abstract int getHeaderLayoutId();

    @Override
    protected void initWidget(View root) {
        //拿到占位布局
        ViewStub stub = (ViewStub) root.findViewById(R.id.view_stub_header);
        stub.setLayoutResource(getHeaderLayoutId());
        stub.inflate();

        super.initWidget(root);

        //初始化面板操作
        mPanelBoss = (AirPanel.Boss) root.findViewById(R.id.lay_content);
        mPanelBoss.setup(new AirPanel.PanelListener() {
            @Override
            public void requestHideSoftKeyboard() {
                //请求隐藏软键盘
                Util.hideKeyboard(mContent);
            }
        });

        mPanelBoss.setOnStateChangedListener(new AirPanel.OnStateChangedListener() {
            @Override
            public void onPanelStateChanged(boolean isOpen) {
                //面板状态改变
                if(isOpen) {
                    onBottomPanelOpened();
                    onAdapterDataChanged();
                }
            }

            @Override
            public void onSoftKeyboardStateChanged(boolean isOpen) {
                //软键盘状态改变
                if(isOpen) {
                    onBottomPanelOpened();
                    onAdapterDataChanged();
                }
            }
        });

        mPanelFragment = (PanelFragment) getChildFragmentManager().findFragmentById(R.id.frag_panel);
        mPanelFragment.setup(this);

        initToolbar();
        initAppbar();
        initEditContent();
        //RecyclerView基本设置
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);

        //添加适配器的监听器进行点击的实现
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Message>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Message message) {
                if (message.getType() == Message.TYPE_AUDIO && holder instanceof ChatFragment.AudiHolder) {
                    mAudioFileCache.download((ChatFragment.AudiHolder) holder, message.getContent());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //进入界面的时候就进行初始化

        mAudioPlayer = new AudioPlayHelper<>(new AudioPlayHelper.RecordPlayListener<AudiHolder>() {
            @Override
            public void onPlayStart(AudiHolder audiHolder) {
                //范型的作用就在于此
                audiHolder.onPlayStart();
            }

            @Override
            public void onPlayStop(AudiHolder audiHolder) {
                //直接停止
                audiHolder.onPlayStop();
            }

            @Override
            public void onPlayError(AudiHolder audiHolder) {
                Application.showToast(R.string.toast_audio_play_error);
            }
        });

        //下载
        mAudioFileCache = new FileCache<>("audio/cache", "mp3", new FileCache.CacheListener<AudiHolder>() {
            @Override
            public void onDownloadSucceed(final AudiHolder holder, final File file) {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        //主线程播放
                        mAudioPlayer.trigger(holder, file.getAbsolutePath());
                    }
                });
            }

            @Override
            public void onDownloadFailed(AudiHolder holder) {
                Application.showToast(R.string.toast_download_error);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAudioPlayer.destroy();
    }

    private void onBottomPanelOpened() {
        if (mAppBarLayout != null)
            mAppBarLayout.setExpanded(false, true);
    }

    @Override
    public boolean onBackPressed() {
        if (mPanelBoss.isOpen()) {
            //关闭面板并且返回true表示自己已经处理了返回消费
            mPanelBoss.closePanel();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    protected void initData() {
        super.initData();
        //初始化Presenter
        mPresenter.start();
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
        //仅仅只需要请求打开即可
        mPanelBoss.openPanel();
        mPanelFragment.showFace();
    }

    @OnClick(R.id.btn_record)
    void onRecordClick() {
        mPanelBoss.openPanel();
        mPanelFragment.showRecord();
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mSubmit.isActivated()) {
            //发送
            String content = mContent.getText().toString();
            mContent.setText("");
            mPresenter.pushText(content);
        } else {
            onMoreClick();
        }
    }

    private void onMoreClick() {
        mPanelBoss.openPanel();
        mPanelFragment.showGallery();
    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        //不需要做任何事情 因为没有占位布局 Recycler是常显示的
        mRecyclerView.smoothScrollToPosition(mAdapter.getItems().size());
    }

    @Override
    public EditText getInputEditText() {
        //返回输入框
        return mContent;
    }

    @Override
    public void onSendGallery(String[] paths) {
        //图片
        mPresenter.pushImages(paths);
    }

    @Override
    public void onRecordDone(File file, long time) {
        //语音
        mPresenter.pushAudio(file.getAbsolutePath(), time);
    }

    //内容的适配器
    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {
            //我发送的在右边 收到的在左边
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());
            switch (message.getType()) {
                //文字内容
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
                //语言内容
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;
                //图片内容
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;
                //其他内容 包括文件
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                //左右都是同一个
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new TextHolder(root);
                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudiHolder(root);
                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);
                //默认情况下 返回的就是Text类型的Holder进行处理
                default:
                    return new TextHolder(root);
            }
        }
    }

    //Holder的基类
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        //允许为空 左边没有  右边有
        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;

        public BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            //进行数据加载
            sender.load();
            //进行头像加载
            mPortrait.setup(Glide.with(ChatFragment.this), sender);

            if (mLoading != null) {
                //当前布局应该是在右边
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {
                    //正常状态 隐藏loading
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    //正在发送中状态
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {
                    //发送失败状态 允许重新发送
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.stop();
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                }
                //当状态是错误状态是才允许点击
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        @OnClick(R.id.im_portrait)
        void onRePushClick() {
            //重新发送
            if (mLoading != null && mPresenter.rePush(mData)) {
                //必须是右边的才有可能重新发送
                //状态改变需要重新刷新界面当前的信息
                updateData(mData);
            }
        }
    }

    //文字的Holder
    class TextHolder extends BaseHolder {
        @BindView(R.id.txt_content)
        TextView mContent;

        public TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            Spannable spannable = new SpannableString(message.getContent());
            //解析表情
            Face.decode(mContent, spannable, (int) Ui.dipToPx(getResources(), 20));
            //把文字设置到布局上
            mContent.setText(spannable);
        }
    }

    //语音的Holder
    class AudiHolder extends BaseHolder {
        @BindView(R.id.txt_content)
        TextView mContent;

        @BindView(R.id.im_audio_track)
        ImageView mAudioTrack;

        public AudiHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            String attach = TextUtils.isEmpty(message.getAttach()) ? "0" : message.getAttach();
            mContent.setText(formatTime(attach));
        }

        void onPlayStart() {
            mAudioTrack.setVisibility(View.VISIBLE);
        }

        void onPlayStop() {
            //占位并隐藏
            mAudioTrack.setVisibility(View.INVISIBLE);
        }

        private String formatTime(String attach) {
            float time;
            try {
                time = Float.parseFloat(attach) / 1000f;

            } catch (Exception e) {
                time = 0;
            }
            //取整为一位小数
            String shortTIme = String.valueOf(Math.round(time * 10f) / 10f);
            shortTIme = shortTIme.replaceAll("[.]0+?$|0+?$", "");
            return String.format("%s″", shortTIme);
        }
    }

    //图片的Holder
    class PicHolder extends BaseHolder {
        @BindView(R.id.im_image)
        ImageView mContent;

        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            //当时图片类型的时候 content中就是具体的地址
            String content = message.getContent();
            Glide.with(ChatFragment.this)
                    .load(content)
                    .fitCenter()
                    .into(mContent);
        }
    }

}
