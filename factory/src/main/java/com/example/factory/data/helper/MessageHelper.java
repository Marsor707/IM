package com.example.factory.data.helper;

import android.os.SystemClock;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.common.Common;
import com.example.common.app.Application;
import com.example.factory.Factory;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.example.factory.net.NetWork;
import com.example.factory.net.RemoteService;
import com.example.factory.net.UploadHelper;
import com.example.utils.PicturesCompressor;
import com.example.utils.StreamUtil;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息工具类
 * Created by marsor on 2017/7/7.
 */

public class MessageHelper {
    // 从本地找消息
    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    //发送是异步进行的
    public static void push(final MsgCreateModel model) {
        Factory.runOnAnsy(new Runnable() {
            @Override
            public void run() {
                //成功状态：如果是一个已发送过的消息 则不能重新发送
                //正在发送状态：如果一个消息正在发送 则不能重新发送
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED)
                    return;


                //我们在发送的时候需要通知界面更新状态 card
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);

                //如果是文件类型的（语言 图片 文件）需先上传后再发送
                //发送文件消息分两部 上传到云服务器 消息push到自己服务器
                if (card.getType() != Message.TYPE_STR) {
                    //不是文字类型
                    if (!card.getContent().startsWith(UploadHelper.ENDPOINT)) {
                        //没有上传到云服务器 还是本地地址
                        String content;
                        switch (card.getType()) {
                            case Message.TYPE_PIC:
                                content = uploadPicture(card.getContent());
                                break;
                            case Message.TYPE_AUDIO:
                                content = uploadAudio(card.getContent());
                                break;
                            default:
                                content = "";
                                break;
                        }
                        if (TextUtils.isEmpty(content)) {
                            //失败
                            card.setStatus(Message.STATUS_FAILED);
                            Factory.getMessageCenter().dispatch(card);
                        }

                        //成功则把网络路径替换
                        card.setContent(content);
                        Factory.getMessageCenter().dispatch(card);
                        //因为卡片的内容更改了 而我们上传到服务器是使用的model
                        //所以model也需要跟着更改
                        model.refreshByCard();
                    }
                }

                //直接发送进行网络调度
                RemoteService service = NetWork.remote();
                service.msgPush(model).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (call != null) {
                                //成功的调度
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            //检查账户是否异常
                            Factory.decodeRspCode(rspModel, null);
                            //走失败流程
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        //通知失败
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }

    //上传图片
    private static String uploadPicture(String path) {
        File file = null;
        try {
            //通过glide的缓存区间解决了图片的外部权限问题
            file = Glide.with(Factory.app())
                    .load(path)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file != null) {
            String cacheDir = Application.getCacheDirFile().getAbsolutePath();
            String tempFile = String.format("%s/image/Cache_%s.png", cacheDir, SystemClock.uptimeMillis());
            try {
                //进行压缩
                if (PicturesCompressor.compressImage(file.getAbsolutePath(), tempFile, Common.Constance.MAX_UPLOAD_IMAGE_LENGTH)) {
                    //上传
                    String ossPath = UploadHelper.uploadImage(tempFile);
                    //清理缓存
                    StreamUtil.delete(tempFile);
                    return ossPath;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    //上传语言
    private static String uploadAudio(String content) {
        //上传语言
        File file=new File(content);
        if(!file.exists()||file.length()<=0)
            return null;

        //上传并返回
        return UploadHelper.uploadAudio(content);
    }

    /**
     * 查询一个消息 这个消息是一个群的最后一条消息
     *
     * @param groupId 群id
     * @return 群聊天中的最后一条消息
     */
    public static Message findLastWithGroup(String groupId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }

    /**
     * 查询一个消息 这个消息是一个人的最后一条消息
     *
     * @param userId userId
     * @return Message
     */
    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }
}
