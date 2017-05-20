package com.example.marsor.push.frags.account;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.example.common.app.Application;
import com.example.common.app.Fragment;
import com.example.common.widget.PortraitView;
import com.example.marsor.push.R;
import com.example.marsor.push.frags.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 更新信息的Fragment
 */
public class UpdateInfoFragment extends Fragment {
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick(){
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void OnSelectedImage(String path) {
                        UCrop.Options options=new UCrop.Options();
                        //设置图片处理的格式jpeg
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        //设置压缩后的图片精读
                        options.setCompressionQuality(96);
                        //得到头像的缓存地址
                        File dPath= Application.getPortraitTmpFile();
                        //发起剪切
                        UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(dPath))
                                .withAspectRatio(1,1)//1:1比例
                                .withMaxResultSize(520,520)//返回的最大尺寸
                                .withOptions(options)//相关参数
                                .start(getActivity());
                    }
                    //show的时候建议使用getChildFragmentManager
                }).show(getChildFragmentManager(),GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 收到从Activity传递过来的回调，然后取出其中的值进行图片加载
        // 如果是我能够处理的类型
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // 通过UCrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 加载uri到当前的头像中
     * @param uri Uri
     */
    private void loadPortrait(Uri uri){
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

    }


}