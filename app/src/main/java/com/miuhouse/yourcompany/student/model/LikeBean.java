package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;

/**
 * Created by kings on 12/13/2016.
 */
public class LikeBean implements Serializable {

    /**
     * 点赞人id
     */
    private String userId;
    /**
     * 点赞人昵称
     */
    private String nickName;
    /**
     * 点赞人头像
     */
    private String headUrl;
    /**
     * 点赞记录id
     */
    private String id;
    /**
     * 贴子id
     */
    private String recordId;
    /**
     * 帖子类型
     */
    private int type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
