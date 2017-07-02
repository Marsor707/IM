package com.example.marsor.push.frags.search;


import com.example.common.app.PresenterFragment;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.search.SearchContract;
import com.example.factory.presenter.search.SearchUserPresenter;
import com.example.marsor.push.R;
import com.example.marsor.push.activities.SearchActivity;

import java.util.List;

/**
 * 搜索人的界面实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter> implements SearchActivity.SearchFragment,SearchContract.UserView{


    public SearchUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void search(String content) {
        //Activity->Fragment->Presenter->Net
        mPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        //数据成功情况下返回数据
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        //初始化presenter
        return new SearchUserPresenter(this);
    }
}
