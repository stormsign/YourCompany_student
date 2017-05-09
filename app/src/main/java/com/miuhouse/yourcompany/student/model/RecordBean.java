package com.miuhouse.yourcompany.student.model;

import android.content.Context;
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
 * Created by kings on 12/13/2016.
 */
public class RecordBean implements Serializable {

    public static final int LIKE_ACCOUNT_NAME = 0;
    public static final int LOCAL_LIKE_ACCOUNT_NAME = 1;
    public static final int LOCAL_NO_LIKE_ACCOUNT_NAME = 2;
    /**
     * 活动记录
     */
    private String id;
    /**
     * 帐号id
     */
    private long userId;
    /**
     * 标签
     */
    private long tagId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户图像
     */
    private String headUrl;
    /**
     * 用户提交时间
     */
    private long createTime;
    /**
     * 图片数组
     */
    private ArrayList<String> images = new ArrayList<String>();
    /**
     * 内容
     */
    private String content;
    /**
     * 是否点赞
     */
    private int likeId;
    /**
     * 评论总数量
     */
    private int commentSize;
    /**
     * 点赞数量
     */
    private int likeSize;
    /**
     * 评论列表
     */
    private List<CommentBean> commentList = new ArrayList<CommentBean>();
    /**
     * 赞列表
     */
    private List<LikeBean> likeList = new ArrayList<LikeBean>();
    /**
     * 自己加的标签
     */
    private boolean isLike = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public int getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(int commentSize) {
        this.commentSize = commentSize;
    }

    public int getLikeSize() {
        return likeSize;
    }

    public void setLikeSize(int likeSize) {
        this.likeSize = likeSize;
    }

    public List<CommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentBean> commentList) {
        this.commentList = commentList;
    }

    public List<LikeBean> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<LikeBean> likeList) {
        this.likeList = likeList;
    }

    /**
     * type
     *
     * @param tvLikeAccount
     */
    public void setLikeAccountName(TextView tvLikeAccount, int type) {
        if (getLikeList().size() > 0) {
            tvLikeAccount.setVisibility(View.VISIBLE);
            tvLikeAccount.setFocusable(false);
            tvLikeAccount.setLongClickable(false);

            tvLikeAccount.setText(getAccountNameStr());
        } else {
            tvLikeAccount.setVisibility(View.GONE);
            tvLikeAccount.setText("");
        }
    }

//    private String getTyle

    /**
     * 返回赞了的用户名字
     *
     * @return
     */
    private SpannableString getAccountNameStr() {
        return showLikeName(showCount());
    }

    private SpannableString getContainMySelfLikeName() {
        if (getLikeId() == 1) {
            LikeBean likeBean = new LikeBean();
            ParentInfo parentInfo = AccountDBTask.getAccount();
//            UserBean userBean = MyApplication.getInstance().getUserBean();
//            likeBean.setHeadUrl(userBean.getHeadUrl());
            assert parentInfo != null;
            likeBean.setNickName(parentInfo.getpName());
            likeBean.setUserId(parentInfo.getId());
            getLikeList().add(0, likeBean);
        }
        return showLikeName(showCount());
    }

    private SpannableString getMySelfCancelLikeName() {
        if (getLikeId() == 0) {
            for (int i = 0; i < getLikeList().size(); i++) {
                if (getLikeList().get(i).getId().equals(AccountDBTask.getAccount().getId())) {
                    getLikeList().remove(i);
                }
            }
        }
        return showLikeName(showCount());
    }


    /**
     * 上限为6个名字
     *
     * @return
     */
    private int showCount() {
        return getLikeList().size() > 6 ? 6 : getLikeList().size();
    }

    public SpannableString showLikeName(int showCount) {

        StringBuilder sbBuilder = new StringBuilder();

        if (showCount < 6) {
            for (int i = 0; i < getLikeSize(); i++) {
                sbBuilder.append(getLikeList().get(i).getNickName()).append("、");
                Log.i("TAG", "sbBuilder=" + sbBuilder.toString());
            }
        } else {
            for (int i = 0; i < showCount; i++) {
                sbBuilder.append(getLikeList().get(i).getNickName()).append("、");
                Log.i("TAG", "sbBuilder=" + sbBuilder.toString());
            }
        }
        String likeUsersStr;
        if (sbBuilder.toString().contains("、")) {
            likeUsersStr = sbBuilder.substring(0, sbBuilder.lastIndexOf("、"));
        } else {
            likeUsersStr = sbBuilder.toString();
        }
        Log.i("TAG", "likeUsersStr=" + likeUsersStr);
        // 第一个赞图标
        // ImageSpan span = new ImageSpan(AppContext.getInstance(),
        // R.drawable.ic_unlike_small);
        // SpannableString spanStr = new SpannableString("");
        // spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        StringBuilder ssb = new StringBuilder(likeUsersStr);
        // ssb.append(likeUsersStr);

        String[] likeUsers = likeUsersStr.split("、");

        if (likeUsers.length < getLikeSize()) {
            ssb.append("等");
            int start = ssb.length();
            String more = getLikeSize() + "人";
            ssb.append(more);
            ssb.append("觉得很赞");
        } else {
            ssb.append("觉得很赞");
        }
        SpannableString spanStr = new SpannableString(ssb.toString());
        spanStr.setSpan(new ForegroundColorSpan(Color.argb(255, 100, 100, 100)), likeUsersStr.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

}
