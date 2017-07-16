package com.example.factory.model.api.group;

import com.example.factory.model.db.User;

import java.util.HashSet;
import java.util.Set;

/**
 * 群创建的Model
 * Created by marsor on 2017/7/16.
 */

public class GroupCreateModel {
    private String name;
    private String desc;
    private String picture;
    private Set<String> users = new HashSet<>();

    public GroupCreateModel(String name, String desc, String picture, Set<String> users) {
        this.name = name;
        this.desc = desc;
        this.picture = picture;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
