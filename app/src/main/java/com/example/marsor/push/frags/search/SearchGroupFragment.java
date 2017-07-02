package com.example.marsor.push.frags.search;


import com.example.common.app.Fragment;
import com.example.marsor.push.R;
import com.example.marsor.push.activities.SearchActivity;

/**
 * 搜索群的界面实现
 */
public class SearchGroupFragment extends Fragment implements SearchActivity.SearchFragment{


    public SearchGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}
