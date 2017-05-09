package com.miuhouse.yourcompany.student.model;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.application.App;

import com.miuhouse.yourcompany.student.db.AccountDBTask;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 12/23/2016.
 */
public class SchoolNewsBean implements Serializable {

    private String id;
    private long schoolId;
    /**
     * 来自某某班级
     */
    private List<ClassEntity> classSet = new ArrayList<>();
    /**
     * 用户id，老师or家长
     */
    private String linkId;

    /**
     * 用户的类型，老师or家长
     */
    private String linkType;

    private String title;

    private String content;

    /**
     * 内容类型1动态，2食谱，3通知
     */
    private String contentType;

    /**
     * 图片集合
     */
    private ArrayList<String> image = new ArrayList<>();

    /**
     * 视频链接
     */
    private String video;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 评论列表
     */
    private List<CommentsBean> comments = new ArrayList<>();

    /**
     * 点赞列表
     */
    private List<CommentsBean> thumbups = new ArrayList<>();

    /**
     * 评论数量
     */
    private int commentNum;

    /**
     * 赞数量
     */
    private int thumbupNum;

    /**
     * 发布人名字
     */
    private String linkName;

    private String headUrl;

    /**
     * 是否点赞
     */
    private String isThumbup;

    /**
     * 是否置顶
     */
    private String isTop;

    /**
     * 显示类型 1照片，2是视频
     */
    private String contentShowtype;

    public List<ClassEntity> getClassSet() {
        return classSet;
    }

    public void setClassSet(
        List<ClassEntity> classSet) {
        this.classSet = classSet;
    }

    public String getContentShowtype() {
        return contentShowtype;
    }

    public void setContentShowtype(String contentShowtype) {
        this.contentShowtype = contentShowtype;
    }

    public String getIsThumbup() {
        return isThumbup;
    }

    public void setIsThumbup(String isThumbup) {
        this.isThumbup = isThumbup;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(
        List<CommentsBean> comments) {
        this.comments = comments;
    }

    public List<CommentsBean> getThumbups() {
        return thumbups;
    }

    public void setThumbups(
        List<CommentsBean> thumbups) {
        this.thumbups = thumbups;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getThumbupNum() {
        return thumbupNum;
    }

    public void setThumbupNum(int thumbupNum) {
        this.thumbupNum = thumbupNum;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    /**
     *
     */
    public void setLikeAccountName(TextView tvLikeAccount, boolean isMySelft) {
        if (getThumbupNum() > 0 || isMySelft) {
            tvLikeAccount.setVisibility(View.VISIBLE);
            tvLikeAccount.setFocusable(false);
            tvLikeAccount.setLongClickable(false);
            tvLikeAccount.setText(getAccountNameStr(isMySelft));
        } else {
            tvLikeAccount.setVisibility(View.GONE);
            tvLikeAccount.setText("");
        }
    }

    public void setLikeAccountName(TextView tvLikeAccount) {
        setLikeAccountName(tvLikeAccount, false);
    }

    //    private String getTyle

    /**
     * 返回赞了的用户名字
     */
    private SpannableString getAccountNameStr(boolean isMySelf) {
        return showLikeName(isMySelf);
    }


    public SpannableString showLikeName(boolean isMySelf) {

        boolean isContain = false;
        StringBuilder sbBuilder = new StringBuilder();
        int size = getThumbups().size();
        for (int i = 0; i < size; i++) {
            Log.i("TAG", "getThumbups=" + getThumbups().get(i).getLinkId());
            if (getThumbups().get(i).getLinkId().equals(App.getInstance().getParentId())) {
                isContain = true;
            }
        }
        if ((!isContain) && getIsThumbup().equals("1")) {
            CommentsBean commentsBean = new CommentsBean();
            ParentInfo parentInfo = App.getInstance().getParentInfo();
            commentsBean.setLinkName(parentInfo.getcName() + " " + parentInfo.getRoleName());
            commentsBean.setLinkId(parentInfo.getParentId());
            getThumbups().add(0, commentsBean);
        }
        if (getIsThumbup().equals("0") && isMySelf) {
            for (int i = 0; i < size; i++) {
                if (getThumbups().get(i).getLinkId().equals(App.getInstance().getParentId())) {
                    getThumbups().remove(i);
                    break;
                }
            }
        }
        for (CommentsBean commentsBean : getThumbups()) {
            sbBuilder.append(commentsBean.getLinkName()).append("、");
        }
        String likeUsersStr;
        if (sbBuilder.toString().contains("、")) {
            likeUsersStr = sbBuilder.substring(0, sbBuilder.lastIndexOf("、"));
        } else {
            likeUsersStr = sbBuilder.toString();
        }
        StringBuilder ssb = new StringBuilder(likeUsersStr);
        String[] likeUsers = likeUsersStr.split("、");

        if (likeUsers.length < getThumbups().size()) {
            ssb.append("等");
            int start = ssb.length();
            String more = getThumbups().size() + "人";
            ssb.append(more);
            ssb.append("觉得很赞");
        } else {
            ssb.append("觉得很赞");
        }
        SpannableString spanStr = new SpannableString(ssb.toString());
        spanStr.setSpan(new ForegroundColorSpan(Color.argb(255, 191, 69, 69)), 0,
            ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }
}
