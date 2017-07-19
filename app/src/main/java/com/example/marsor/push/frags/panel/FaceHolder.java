package com.example.marsor.push.frags.panel;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.face.Face;
import com.example.marsor.push.R;

import butterknife.BindView;

/**
 * Created by marsor on 2017/7/18.
 */

public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {
    @BindView(R.id.im_face)
    ImageView mFace;

    public FaceHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(Face.Bean bean) {
        if (bean != null && (
                (bean.preview instanceof Integer) || //drawable资源
                bean.preview instanceof String)) { //zip包资源
            Glide.with(itemView.getContext())
                    .load(bean.preview)
                    .asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .placeholder(R.drawable.default_face)
                    .into(mFace);
        }
    }
}
