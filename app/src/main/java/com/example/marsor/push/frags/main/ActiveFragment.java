package com.example.marsor.push.frags.main;


import com.example.common.app.Fragment;
import com.example.common.widget.GalleyView;
import com.example.marsor.push.R;

import butterknife.BindView;

public class ActiveFragment extends Fragment {
    @BindView(R.id.galleyView)
    GalleyView mGalley;

    public ActiveFragment() {
        // Required empty public constructor
    }

    
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        mGalley.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
