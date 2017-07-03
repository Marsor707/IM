package com.example.marsor.push.frags.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.app.PresenterFragment;
import com.example.common.widget.EmptyView;
import com.example.common.widget.PortraitView;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.search.SearchContract;
import com.example.factory.presenter.search.SearchUserPresenter;
import com.example.marsor.push.R;
import com.example.marsor.push.activities.SearchActivity;

import java.util.List;

import butterknife.BindView;

/**
 * 搜索人的界面实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter> implements SearchActivity.SearchFragment, SearchContract.UserView {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        //初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                //返回cell布局的id
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });
        //初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        //发起首次搜索
        search("");
    }

    @Override
    public void search(String content) {
        //Activity->Fragment->Presenter->Net
        mPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        //数据成功情况下返回数据
        mAdapter.replace(userCards);
        //如果有数据则显示ok 没有则显示空布局
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        //初始化presenter
        return new SearchUserPresenter(this);
    }

    /**
     * 每一个cell的布局操作
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_follow)
        ImageView mFollow;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(UserCard userCard) {
            Glide.with(SearchUserFragment.this)
                    .load(userCard.getPortrait())
                    .centerCrop()
                    .into(mPortraitView);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.isFollow());
        }
    }
}
