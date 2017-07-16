package com.example.factory.presenter.group;

import com.example.factory.model.Author;
import com.example.factory.presenter.BaseContract;

/**
 * 群创建的契约
 * Created by marsor on 2017/7/15.
 */

public interface GroupCreateContract {
    interface Presenter extends BaseContract.Presenter {
        //创建
        void create(String name, String desc, String picture);

        //更改选中状态
        void changeSelect(ViewModel model,boolean isSelected);
    }

    interface View extends BaseContract.RecyclerView<Presenter,ViewModel> {
        //创建成功
        void onCreateSucceed();
    }

    class ViewModel {
        //用户信息
        public Author author;
        //是否选中
        public boolean isSelected;
    }

}
