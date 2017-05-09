package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.listener.OnListItemLongClick;
import com.miuhouse.yourcompany.student.model.ClassEntity;
import com.miuhouse.yourcompany.student.model.CommentsBean;
import com.miuhouse.yourcompany.student.model.SchoolNewsBean;
import com.miuhouse.yourcompany.student.presenter.LikeOrCommentPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ILikeOrCommentPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.DetailTwitterActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.GalleryActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.UnreadMsgActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.VideoPlayerActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ICommentingView;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;

import java.util.List;

/**
 * Created by kings on 12/12/2016.
 */
public class TwitterAdapter extends BaseRVAdapter<SchoolNewsBean> implements ICommentingView {

    private ILikeOrCommentPresenter likeOrCommentPresenter;
    /**
     * 未读消息title
     */
    private static final int UNREADMSG_TITLE = 1;

    /**
     * 内容
     */
    private static final int CONTENT = 2;

    /**
     * 置顶
     */
    private static final int TOP = 3;

    private boolean displayUnread;
    private int unreadCount;
    private ViewWrapper wrapper;
    private int size;

    public TwitterAdapter(Context context, List<SchoolNewsBean> list) {

        super(context, list);
        likeOrCommentPresenter = new LikeOrCommentPresenter(this);
    }

    public TwitterAdapter(Context context, List<SchoolNewsBean> list,
        OnListItemClick onListItemClick) {

        super(context, list, onListItemClick);
        likeOrCommentPresenter = new LikeOrCommentPresenter(this);
    }

    public TwitterAdapter(Context context, List<SchoolNewsBean> list,
        OnListItemClick onListItemClick, OnListItemLongClick onListItemLongClick) {

        super(context, list, onListItemClick, onListItemLongClick);
        likeOrCommentPresenter = new LikeOrCommentPresenter(this);
    }

    public void setSize(int size) {

        this.size = size;
    }

    @Override public int getItemViewType(int position) {

        if (position == 0) {
            return UNREADMSG_TITLE;
        } else if (position <= size) {
            return TOP;
        } else {
            return CONTENT;
        }
    }

    public SchoolNewsBean getItem(int position) {

        return list.get(position);
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == UNREADMSG_TITLE) {
            return new UnreadMsgHolder(
                LayoutInflater.from(context).inflate(R.layout.item_title_unreadmsg, parent, false));
        } else if (viewType == CONTENT) {
            return new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_list_twitter, parent, false));
        } else {
            return new IsTopHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_list_twitter_top, parent, false));
        }
    }

    public void setDisplayUnread(boolean displayUnread) {

        this.displayUnread = displayUnread;
        if (wrapper != null) {
            if (displayUnread && unreadCount > 0) {
                slipOutUnread(wrapper);
                notifyItemChanged(0);
            } else if (!displayUnread && unreadCount > 0) {
                slipInUnread(wrapper);
            }
        }
    }

    @Override public int getItemCount() {

        return list.size() + 1;
    }

    public boolean isDisplayUnread() {

        return displayUnread;
    }

    public void setUnreadCount(int unreadCount) {

        this.unreadCount = unreadCount;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof UnreadMsgHolder) {
            final UnreadMsgHolder msgHolder = (UnreadMsgHolder) holder;
            msgHolder.unread.setText(unreadCount + "条新消息");
            wrapper = new ViewWrapper(msgHolder.content);
            if (displayUnread && unreadCount > 0) {
                displayUnread(wrapper);
            } else {
                hideUnread(wrapper);
            }
            msgHolder.unread.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {

                    setDisplayUnread(false);
                    SPUtils.saveData(Constants.UNREAD_DONGTAI, 0);
                    if (unreadCount == 1) {
                        context.startActivity(
                            new Intent(context, UnreadMsgActivity.class).putExtra("schoolNewsId",
                                SPUtils.getData(Constants.LATEST_DONGTAI, null)));
                    } else if (unreadCount > 1) {
                        context.startActivity(new Intent(context, UnreadMsgActivity.class));
                    }
                    unreadCount = 0;
                }
            });
        } else if (holder instanceof ViewHolder) {

            final ViewHolder viewHolder = (ViewHolder) holder;
            position--;
            final SchoolNewsBean schoolNewsBean = list.get(position);
            viewHolder.itemView.setTag(position);
            buildCommentLiear(schoolNewsBean, viewHolder);
            viewHolder.accountName.setText(schoolNewsBean.getLinkName());
            //buildComeFormClass(viewHolder.tvTweetClass, schoolNewsBean.getClassSet());

            if (Util.isEmpty(schoolNewsBean.getContent())) {
                viewHolder.twitterContent.setVisibility(View.GONE);
            } else {
                viewHolder.twitterContent.setVisibility(View.VISIBLE);
            }

            viewHolder.twitterContent.setText(schoolNewsBean.getContent());

            if (schoolNewsBean.getContentShowtype() != null && (schoolNewsBean.getContentShowtype()
                .equals("2"))) {
                viewHolder.imgIconPlay.setVisibility(View.VISIBLE);
                viewHolder.contentPicMulti.setVisibility(View.GONE);
                viewHolder.framePic.setVisibility(View.VISIBLE);
                Glide.with(context)
                    .load(schoolNewsBean.getImage().get(0))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(viewHolder.contentPic);
                //Glide.with(context).load(schoolNewsBean.getImage().get(0)).asBitmap().into(
                //    new SimpleTarget<Bitmap>() {
                //      @Override
                //      public void onResourceReady(Bitmap resource,
                //          GlideAnimation<? super Bitmap> glideAnimation) {
                //        //DefaultTransform defaultTransform = new DefaultTransform(Util.dip2px(context, 180), 0, ScaleMode.CenterCrop);
                //        //defaultTransform.transform(resource);
                //        viewHolder.contentPic.setImageBitmap(
                //            new DefaultTransform(Util.dip2px(context, 200), 0,
                //                ScaleMode.FixXY).transform(resource));
                //      }
                //    });

            } else if ((schoolNewsBean.getImage() != null
                && schoolNewsBean.getImage().size() == 1)) {
                viewHolder.imgIconPlay.setVisibility(View.GONE);
                viewHolder.contentPicMulti.setVisibility(View.GONE);
                viewHolder.framePic.setVisibility(View.VISIBLE);
                Glide.with(context)
                    .load(schoolNewsBean.getImage().get(0))
                    .placeholder(R.drawable.pic_bg)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(viewHolder.contentPic);
            } else {
                viewHolder.framePic.setVisibility(View.GONE);
                viewHolder.contentPicMulti.setVisibility(View.VISIBLE);
                buildMultiPic(schoolNewsBean, viewHolder.contentPicMulti);
            }

            buildHeadPic(viewHolder.accountAvatar, schoolNewsBean.getHeadUrl(),
                schoolNewsBean.getContentType(), viewHolder.accountName,
                schoolNewsBean.getLinkName());

            viewHolder.tvTweetTime.setText(Util.friendly_time(schoolNewsBean.getCreateTime()));
            if (schoolNewsBean.getIsThumbup().equals("1")) {
                viewHolder.linearLikeState.setBackgroundResource(
                    R.drawable.comment_border_round_enable);
                viewHolder.tvLike.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.btnLike.setImageResource(R.drawable.wutuo_ico_zan_w);
            } else {
                viewHolder.linearLikeState.setBackgroundResource(R.drawable.comment_border_round);
                viewHolder.tvLike.setTextColor(
                    context.getResources().getColor(R.color.textDarkfive));
                viewHolder.btnLike.setImageResource(R.drawable.wutuo_ico_zan);
            }

            int visibility = schoolNewsBean.getThumbupNum() == 0 ? View.GONE : View.VISIBLE;
            viewHolder.imgIconLike.setVisibility(visibility);
            viewHolder.view.setVisibility(visibility);

            viewHolder.likeView.setVisibility(
                schoolNewsBean.getCommentNum() == 0 ? View.GONE : View.VISIBLE);

            schoolNewsBean.setLikeAccountName(viewHolder.likeAccountName);
            viewHolder.linearLikeState.setTag(position);

            /**
             * 点赞
             */
            viewHolder.linearLikeState.setOnClickListener(getListener(viewHolder, schoolNewsBean));
            viewHolder.contentPic.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {

                    if (schoolNewsBean.getContentShowtype() != null
                        && schoolNewsBean.getContentShowtype().equals("2")) {
                        context.startActivity(
                            new Intent(context, VideoPlayerActivity.class).putExtra("url",
                                schoolNewsBean.getVideo()));
                    } else {
                        Intent intent = new Intent(context, GalleryActivity.class);
                        intent.putStringArrayListExtra("urls", schoolNewsBean.getImage());
                        context.startActivity(intent);
                    }
                }
            });

            /**
             *评论
             */
            viewHolder.linearCommentBtn.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {

                    Intent intent = new Intent(context, DetailTwitterActivity.class);
                    intent.putExtra("softKeyboard", true);
                    intent.putExtra("schoolNewsBean", schoolNewsBean);
                    context.startActivity(intent);
                }
            });
        } else {
            position--;
            final SchoolNewsBean schoolNewsBean = list.get(position);

            final IsTopHolder isTopHolder = (IsTopHolder) holder;
            isTopHolder.itemView.setTag(position);
            if (!Util.isEmpty(schoolNewsBean.getContent())) {
                isTopHolder.tvTitle.setText(schoolNewsBean.getContent());
            } else {
                if (schoolNewsBean.getImage() != null) {
                    isTopHolder.tvTitle.setText("发布了" + schoolNewsBean.getImage().size() + "张图片");
                }
            }
        }
    }

    private void slipOutUnread(final ViewWrapper wrapper) {

        if (wrapper.getV().getVisibility() != View.VISIBLE) {
            wrapper.getV().setVisibility(View.VISIBLE);
        }
        ValueAnimator heightA =
            ObjectAnimator.ofFloat(wrapper, wrapper.HEIGHT, Util.dip2px(context, 1),
                Util.dip2px(context, 40));
        ValueAnimator alphaA = ObjectAnimator.ofFloat(wrapper, wrapper.ALPHA, 0f, 1.0f);
        heightA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override public void onAnimationUpdate(ValueAnimator animation) {

                wrapper.getV().invalidate();
            }
        });
        alphaA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override public void onAnimationUpdate(ValueAnimator animation) {

                wrapper.getV().invalidate();
            }
        });
        heightA.setDuration(200);
        alphaA.setDuration(200);
        AnimatorSet as = new AnimatorSet();
        as.play(heightA).with(alphaA);
        as.start();
    }

    private void slipInUnread(final ViewWrapper wrapper) {

        ValueAnimator heightA =
            ObjectAnimator.ofFloat(wrapper, wrapper.HEIGHT, Util.dip2px(context, 40),
                Util.dip2px(context, 1));
        ValueAnimator alphaA = ObjectAnimator.ofFloat(wrapper, wrapper.ALPHA, 1.0f, 0f);
        heightA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override public void onAnimationUpdate(ValueAnimator animation) {

                wrapper.getV().invalidate();
            }
        });
        alphaA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override public void onAnimationUpdate(ValueAnimator animation) {

                wrapper.getV().invalidate();
            }
        });
        heightA.setDuration(200);
        alphaA.setDuration(200);
        AnimatorSet as = new AnimatorSet();

        as.play(heightA).with(alphaA);
        as.start();
    }

    private void hideUnread(ViewWrapper wrapper) {

        wrapper.setHeight(Util.dip2px(context, 1));
        wrapper.setAlpha(0f);
    }

    private void displayUnread(ViewWrapper wrapper) {

        wrapper.setHeight(Util.dip2px(context, 40));
        wrapper.setAlpha(1f);
    }

    public void buildCommentLiear(SchoolNewsBean schoolNewsBean, final ViewHolder vh) {

        if (schoolNewsBean.getComments() != null && schoolNewsBean.getComments().size() > 0) {
            vh.accountCommentList.setVisibility(View.VISIBLE);
            if (vh.accountCommentList.getChildCount() > 0) {
                vh.accountCommentList.removeAllViews();
            }
            for (int i = schoolNewsBean.getComments().size() - 1; i >= 0; i--) {
                final int index = i;
                TextView tvComment = (TextView) LayoutInflater.from(context)
                    .inflate(R.layout.layout_comment_text, vh.accountCommentList, false);
                CommentsBean comment = schoolNewsBean.getComments().get(i);
                if (!TextUtils.isEmpty(comment.getLinkedName())) {
                    SpannableString spannableStr = new SpannableString(comment.getLinkName()
                        + "回复"
                        + comment.getLinkedName()
                        + ": "
                        + comment.getComment());
                    // Color.argb(alpha, red, green, blue)
                    spannableStr.setSpan(new ForegroundColorSpan(Color.argb(255, 192, 69, 69)),
                        comment.getLinkName().length() + 2,
                        comment.getLinkName().length() + comment.getLinkedName().length() + 2,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableStr.setSpan(new ForegroundColorSpan(Color.argb(255, 192, 69, 69)), 0,
                        comment.getLinkName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvComment.setText(spannableStr);
                } else {
                    SpannableString spannableStr =
                        new SpannableString(comment.getLinkName() + ": " + comment.getComment());
                    // Color.argb(alpha, red, green, blue)
                    // Log.i("TAG", "comment.getNickName=" + comment.getNickName());
                    spannableStr.setSpan(new ForegroundColorSpan(Color.argb(255, 192, 69, 69)), 0,
                        comment.getLinkName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvComment.setText(spannableStr);
                }
                vh.accountCommentList.addView(tvComment);
            }
        } else {
            vh.accountCommentList.setVisibility(View.GONE);
        }
    }

    @Override public void showLoading(String msg) {

    }

    @Override public void showError(int type) {

    }

    @Override public void hideLoading() {

    }

    @Override public void hideError() {

    }

    @Override public void netError() {

    }

    @Override public void onTokenExpired() {

    }

    @Override public ProgressDialog showWaitDialog() {

        return null;
    }

    @Override public ProgressDialog showWaitDialog(int msg) {

        return null;
    }

    @Override public ProgressDialog showWaitDialog(String msg) {

        return null;
    }

    @Override public void hideWaitDialog() {

    }

    @Override public void showError(int type, CommentsBean mCommentsBean) {

    }

    class IsTopHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        public IsTopHolder(View itemView) {

            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {

                    onListItemClick.onItemClick((Integer) v.getTag());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override public boolean onLongClick(View v) {

                    onListItemLongClick.onItemLongClick((Integer) v.getTag());
                    return true;
                }
            });
        }
    }

    class UnreadMsgHolder extends RecyclerView.ViewHolder {

        TextView unread;
        LinearLayout content;

        public UnreadMsgHolder(View itemView) {

            super(itemView);
            unread = (TextView) itemView.findViewById(R.id.unreadCount);
            content = (LinearLayout) itemView.findViewById(R.id.content);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircularImageViewHome accountAvatar;
        private TextView accountName;
        private TextView twitterContent;
        private ImageView contentPic;
        private GridLayout contentPicMulti;
        private TextView tvTweetTime;
        //private TextView tvTweetClass;
        private TextView likeCount;
        private TextView tvLike;
        private TextView likeAccountName;
        private LinearLayout accountCommentList;
        private View likeView;
        private View view;
        private LinearLayout linearLikeState;
        private ImageView btnLike;
        private ImageView imgIconLike;
        private FrameLayout framePic;
        private ImageView imgIconPlay;
        private LinearLayout linearCommentBtn;

        public ViewHolder(View itemView) {

            super(itemView);
            accountAvatar = (CircularImageViewHome) itemView.findViewById(R.id.avatar);
            accountName = (TextView) itemView.findViewById(R.id.account_name);
            twitterContent = (TextView) itemView.findViewById(R.id.twitter_content);
            tvTweetTime = (TextView) itemView.findViewById(R.id.tv_tweet_time);
            //tvTweetClass = (TextView) itemView.findViewById(R.id.tv_tweet_class);
            likeCount = (TextView) itemView.findViewById(R.id.like_count);
            tvLike = (TextView) itemView.findViewById(R.id.tv_like);
            contentPicMulti = (GridLayout) itemView.findViewById(R.id.content_pic_multi);
            contentPic = (ImageView) itemView.findViewById(R.id.content_pic);
            btnLike = (ImageView) itemView.findViewById(R.id.btn_like);
            likeAccountName = (TextView) itemView.findViewById(R.id.like_account_name);
            accountCommentList = (LinearLayout) itemView.findViewById(R.id.comment_linear);
            likeView = (View) itemView.findViewById(R.id.like_view);
            view = (View) itemView.findViewById(R.id.view);
            imgIconLike = (ImageView) itemView.findViewById(R.id.img_icon_like);
            linearLikeState = (LinearLayout) itemView.findViewById(R.id.linear_like_state);
            linearCommentBtn = (LinearLayout) itemView.findViewById(R.id.linear_comment);
            framePic = (FrameLayout) itemView.findViewById(R.id.frame_pic);
            imgIconPlay = (ImageView) itemView.findViewById(R.id.img_icon_play);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {

                    onListItemClick.onItemClick((Integer) v.getTag());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override public boolean onLongClick(View v) {

                    onListItemLongClick.onItemLongClick((Integer) v.getTag());
                    return true;
                }
            });
        }
    }

    private void buildMultiPic(final SchoolNewsBean schoolNewsBean, final GridLayout gridLayout) {

        if (schoolNewsBean.getImage() != null) {
            final int count = schoolNewsBean.getImage().size();
            for (int i = 0; i < count; i++) {

                final ImageView pic = (ImageView) gridLayout.getChildAt(i);

                pic.setVisibility(View.VISIBLE);

                Glide.with(context)
                    .load(schoolNewsBean.getImage().get(i))
                    .placeholder(R.drawable.pic_small_bg)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(pic);

                final int position = i;
                pic.setOnClickListener(new View.OnClickListener() {

                    @Override public void onClick(View v) {

                        Intent intent = new Intent(context, GalleryActivity.class);
                        intent.putStringArrayListExtra("urls", schoolNewsBean.getImage());
                        intent.putExtra("position", position);
                        context.startActivity(intent);
                    }
                });
            }
            if (count < 9) {
                gridLayout.setVisibility(View.VISIBLE);
                ImageView pic;
                switch (count) {
                    case 8:
                        pic = (ImageView) gridLayout.getChildAt(8);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 7:
                        for (int i = 8; i > 6; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 6:
                        for (int i = 8; i > 5; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        break;
                    case 5:
                        for (int i = 8; i > 5; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) gridLayout.getChildAt(5);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        for (int i = 8; i > 5; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) gridLayout.getChildAt(5);
                        pic.setVisibility(View.INVISIBLE);
                        pic = (ImageView) gridLayout.getChildAt(4);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        for (int i = 8; i > 2; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        for (int i = 8; i > 2; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) gridLayout.getChildAt(2);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        } else {
            gridLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 家长版，不需要来自某某班
     */
    //private void buildComeFormClass(TextView tvTweetClass, List<ClassEntity> classSet) {
    //
    //    StringBuilder strComeClass = new StringBuilder("来自");
    //    for (ClassEntity str : classSet) {
    //        Log.i("TAG", "classSet=" + str.getId());
    //        strComeClass.append(str.getName());
    //    }
    //    tvTweetClass.setText(strComeClass.toString());
    //}
    private void buildHeadPic(ImageView headPicView, String headUrl, String contentType,
        TextView tvName, String strName) {

        if (contentType.equals("1")) {
            tvName.setText(strName);
            if (headUrl == null) {
                Glide.with(context).load(R.mipmap.ico_head_default).centerCrop().into(headPicView);
            } else {
                Glide.with(context)
                    .load(headUrl)
                    .centerCrop()
                    .error(R.mipmap.ico_head_default)
                    .into(headPicView);
            }
            tvName.setTextColor(Color.parseColor("#c04444"));
        } else if (contentType.equals("2")) {
            tvName.setText("食谱");
            tvName.setTextColor(Color.parseColor("#ff5c30"));
            Glide.with(context).load(R.drawable.ico_shipu).centerCrop().into(headPicView);
        } else if (contentType.equals("3")) {
            tvName.setText("通知");
            tvName.setTextColor(Color.parseColor("#4096ff"));
            Glide.with(context).load(R.drawable.ico_tongzhi).centerCrop().into(headPicView);
        }
    }

    private View.OnClickListener getListener(final ViewHolder viewHolder,
        final SchoolNewsBean schoolNewsBean) {

        return new View.OnClickListener() {

            @Override public void onClick(View v) {

                if (schoolNewsBean.getIsThumbup().equals("1")) {
                    schoolNewsBean.setIsThumbup("0");
                    Log.i("TAG", "thumbupNum=" + schoolNewsBean.getThumbupNum());
                    if (schoolNewsBean.getThumbupNum() == 1) {
                        viewHolder.imgIconLike.setVisibility(View.GONE);
                        viewHolder.view.setVisibility(View.GONE);
                    }
                    schoolNewsBean.setThumbupNum(schoolNewsBean.getThumbups().size() - 1);
                    viewHolder.linearLikeState.setBackgroundResource(
                        R.drawable.comment_border_round);
                    viewHolder.tvLike.setTextColor(
                        context.getResources().getColor(R.color.textDarkfive));
                    viewHolder.btnLike.setImageResource(R.drawable.wutuo_ico_zan);

                    likeOrCommentPresenter.cancelLike(App.getInstance().getParentId(),
                        schoolNewsBean.getId());
                    schoolNewsBean.setLikeAccountName(viewHolder.likeAccountName, true);
                } else {
                    schoolNewsBean.setIsThumbup("1");
                    if (schoolNewsBean.getThumbupNum() == 0) {
                        viewHolder.imgIconLike.setVisibility(View.VISIBLE);
                        viewHolder.view.setVisibility(View.VISIBLE);
                    }
                    schoolNewsBean.setThumbupNum(schoolNewsBean.getThumbups().size() + 1);

                    viewHolder.linearLikeState.setBackgroundResource(
                        R.drawable.comment_border_round_enable);
                    viewHolder.tvLike.setTextColor(context.getResources().getColor(R.color.white));
                    viewHolder.btnLike.setImageResource(R.drawable.wutuo_ico_zan_w);
                    // TODO: 2017/1/19
                    likeOrCommentPresenter.getLikeOrComment(App.getInstance().getParentId(),
                        schoolNewsBean.getId(), "2", App.getInstance().getParentId(), "3",
                        App.getInstance().getParentInfo().getcName() + " " + App.getInstance()
                            .getParentInfo()
                            .getRoleName());
                }
                schoolNewsBean.setLikeAccountName(viewHolder.likeAccountName);
            }
        };
    }

    //    @Override
    //    public int getItemCount() {
    //        return 10;
    //    }

    class ViewWrapper {

        public final String HEIGHT = "height";
        public final String ALPHA = "alpha";
        View v;
        float height;
        float alpha;

        public ViewWrapper(View v) {

            this.v = v;
        }

        public View getV() {

            return v;
        }

        public float getHeight() {

            return height;
        }

        public void setHeight(float height) {

            this.height = height;
            v.getLayoutParams().height = (int) height;
            v.requestLayout();
        }

        public float getAlpha() {

            return alpha;
        }

        public void setAlpha(float alpha) {

            this.alpha = alpha;
            v.setAlpha(alpha);
            v.requestLayout();
        }
    }
}
