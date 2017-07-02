package com.example.factory.presenter.search;

import com.example.factory.presenter.BasePresenter;

/**
 * 搜索人的实现
 * Created by marsor on 2017/7/2.
 */

public class SearchUserPresenter extends BasePresenter<SearchContract.UserView> implements SearchContract.Presenter{
    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
