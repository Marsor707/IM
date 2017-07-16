package com.example.factory.net;

import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.api.group.GroupCreateModel;
import com.example.factory.model.api.group.GroupMemberAddModel;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 网络请求的所有接口
 * Created by marsor on 2017/5/29.
 */

public interface RemoteService {
    /**
     * 网络请求 一个注册接口
     *
     * @param model RegisterModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     *
     * @param model LoginModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备id
     *
     * @param pushId 设备id
     * @return RspModel<AccountRspModel>
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    /**
     * 用户更新的接口
     *
     * @param model UserUpdateModel
     * @return RspModel<UserCard>
     */
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    /**
     * 用户搜索的接口
     *
     * @param name 用户名
     * @return RspModel<List<UserCard>>
     */
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);

    /**
     * 用户关注的接口
     *
     * @param userId 被关注的id
     * @return RspModel<UserCard>
     */
    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId") String userId);

    /**
     * 获取联系人列表
     *
     * @return RspModel<List<UserCard>>
     */
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();

    /**
     * 查询一个联系人
     *
     * @param userId 用户id
     * @return RspModel<UserCard>
     */
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    /**
     * 发送消息的接口
     *
     * @param model MsgCreateModel
     * @return RspModel<MessageCard>
     */
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    /**
     * 创建群
     *
     * @param model GroupCreateModel
     * @return RspModel<GroupCard>
     */
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    /**
     * 找群
     *
     * @param groupId 群id
     * @return RspModel<GroupCard>
     */
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);

    /**
     * 群搜索
     *
     * @param name 群名
     * @return RspModel<List<GroupCard>>
     */
    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    /**
     * 群列表
     *
     * @param date 日期
     * @return RspModel<List<GroupCard>>
     */
    @GET("group/list/{date}")
    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);


    /**
     * 群成员
     *
     * @param groupId 群id
     * @return RspModel<List<GroupMemberCard>>
     */
    @GET("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);

    /**
     * 添加群成员
     *
     * @param groupId 群id
     * @param model   GroupMemberAddModel
     * @return RspModel<List<GroupMemberCard>>
     */
    @POST("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId, @Body GroupMemberAddModel model);
}
