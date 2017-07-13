package com.example.factory.presenter.message;

import com.example.factory.model.db.Session;
import com.example.factory.presenter.BaseContract;

/**
 * Created by marsor on 2017/7/13.
 */

public interface SessionContract {
    interface Presenter extends BaseContract.Presenter{

    }

    interface View extends BaseContract.RecyclerView<Presenter,Session>{

    }
}
