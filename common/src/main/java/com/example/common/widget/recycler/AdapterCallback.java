package com.example.common.widget.recycler;

/**
 * Created by marsor on 2017/5/15.
 */

public interface AdapterCallback<Data> {
    void update(Data data,RecyclerAdapter.ViewHolder<Data> holder);
}
