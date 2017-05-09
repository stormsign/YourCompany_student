package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;

/**
 * Created by kings on 12/23/2016.
 */
public class CommentsBean implements Serializable {

    private String id;
    private String fId;
    private String newsId;
    private String commentType;
    private String linkId;
    private String linkType;
    private String linkName;

    private String linkedId;
    private String linkedType;
    private String linkedName;
    private String comment;
    private String readStatus;
    private long commentTime;
    private String linkHeadUrl;

    public String getLinkHeadUrl() {
        return linkHeadUrl;
    }

    public void setLinkHeadUrl(String linkHeadUrl) {
        this.linkHeadUrl = linkHeadUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }

    public String getLinkedType() {
        return linkedType;
    }

    public void setLinkedType(String linkedType) {
        this.linkedType = linkedType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLinkedName() {
        return linkedName;
    }

    public void setLinkedName(String linkedName) {
        this.linkedName = linkedName;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }
}

