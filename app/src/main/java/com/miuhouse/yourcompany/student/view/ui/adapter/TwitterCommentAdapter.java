package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
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
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.listener.OnListItemLongClick;
import com.miuhouse.yourcompany.student.model.ClassEntity;
import com.miuhouse.yourcompany.student.model.CommentsBean;
import com.miuhouse.yourcompany.student.model.SchoolNewsBean;
import com.miuhouse.yourcompany.student.presenter.LikeOrCommentPresenter;
import com.miuhouse.yourcompany.student.presenter.SetTopPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ILikeOrCommentPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ISetTopPresenter;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.DetailTwitterActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.GalleryActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.VideoPlayerActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ICommentingView;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;
import java.util.List;

/**
 * Created by kings on 12/22/2016.
 */
public class TwitterCommentAdapter extends BaseRVAdapter<CommentsBean>
    implements BaseView, ICommentingView {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_NOTIFICATON = 2;

    private View mHeaderView;
    private int type;
    private SchoolNewsBean schoolNewsBean;
    private ISetTopPresenter setTopPresenter;
    private ILikeOrCommentPresenter likeOrCommentPresenter;
    private boolean isResult;

    public TwitterCommentAdapter(Context context, List<CommentsBean> list,
        OnListItemClick onListItemClick,
        SchoolNewsBean schoolNewsBean) {
        super(context, list, onListItemClick);
        this.schoolNewsBean = schoolNewsBean;
        setTopPresenter = new SetTopPresenter(this);
        likeOrCommentPresenter = new LikeOrCommentPresenter(this);
    }
    public TwitterCommentAdapter(Context context, List<CommentsBean> list,
        OnListItemClick onListItemClick, OnListItemLongClick onListItemLongClick,
        SchoolNewsBean schoolNewsBean) {
        super(context, list, onListItemClick, onListItemLongClick);
        this.schoolNewsBean = schoolNewsBean;
        setTopPresenter = new SetTopPresenter(this);
        likeOrCommentPresenter = new LikeOrCommentPresenter(this);
    }

    public CommentsBean getItem(int position) {
        return list.get(position);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public View getmHeaderView() {
        return mHeaderView;
    }

    public void setmHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
    }

    public boolean isResult() {
        return isResult;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        } else if (mHeaderView != null && viewType == TYPE_NOTIFICATON) {
            return new HeaderNotificationHolder(mHeaderView);
        } else {
            return new ViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.list_item_cmment_twitter, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            int index = position;
            if (mHeaderView != null) {
                index -= 1;
            }
            CommentsBean commentsBean = list.get(index);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.accountName.setText(commentsBean.getLinkName());
            viewHolder.tvCommentTime.setText(Util.friendly_time(commentsBean.getCommentTime()));
            viewHolder.itemView.setTag(position);
            if (commentsBean.getLinkedId() != null) {
                SpannableString spannableString = new SpannableString(
                    "回复" + commentsBean.getLinkedName() + " " + commentsBean.getComment());
                spannableString.setSpan(new ForegroundColorSpan(Color.argb(255, 192, 69, 69)), 2,
                    2 + commentsBean.getLinkedName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.commentContent.setText(spannableString);
            } else {
                viewHolder.commentContent.setText(commentsBean.getComment());
            }

            if (commentsBean.getLinkHeadUrl() == null) {
                Glide.with(context)
                    .load(R.mipmap.ico_head_default)
                    .centerCrop()
                    .into(viewHolder.accountAvatar);
            } else {
                Glide.with(context)
                    .load(commentsBean.getLinkHeadUrl())
                    .centerCrop()
                    .error(R.mipmap.ico_head_default)
                    .into(viewHolder.accountAvatar);
            }
        } else if (getItemViewType(position) == TYPE_HEADER) {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            if (schoolNewsBean.getCommentNum() > 0) {
                headerViewHolder.tvCommentNumber.setText(schoolNewsBean.getCommentNum() + "条评论");
            }
            headerViewHolder.tvTweetClass.setText("来自" + schoolNewsBean.getClassSet());
            if (schoolNewsBean.getContentShowtype() != null && (schoolNewsBean.getContentShowtype()
                .equals("2"))) {
                headerViewHolder.imgIconPlay.setVisibility(View.VISIBLE);
                headerViewHolder.contentPicMulti.setVisibility(View.GONE);
                headerViewHolder.framePic.setVisibility(View.VISIBLE);
                Glide.with(context)
                    .load(schoolNewsBean.getImage().get(0))
                    .centerCrop()
                    .override(Util.dip2px(context, 180), Util.dip2px(context, 180))
                    .into(headerViewHolder.contentPic);
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
                headerViewHolder.imgIconPlay.setVisibility(View.GONE);
                headerViewHolder.contentPicMulti.setVisibility(View.GONE);
                headerViewHolder.framePic.setVisibility(View.VISIBLE);
                Glide.with(context)
                    .load(schoolNewsBean.getImage().get(0))
                    .centerCrop()
                    .override(Util.dip2px(context, 180), Util.dip2px(context, 180))
                    .into(headerViewHolder.contentPic);
            } else {
                headerViewHolder.framePic.setVisibility(View.GONE);
                headerViewHolder.contentPicMulti.setVisibility(View.VISIBLE);
                buildMultiPic(schoolNewsBean, headerViewHolder.contentPicMulti);
            }
            headerViewHolder.accountName.setText(schoolNewsBean.getLinkName());

            buildComeFormClass(headerViewHolder.tvTweetClass, schoolNewsBean.getClassSet());

            int visibility = schoolNewsBean.getThumbupNum() == 0 ? View.GONE : View.VISIBLE;
            headerViewHolder.imgIconLike.setVisibility(visibility);
            headerViewHolder.view.setVisibility(visibility);

            if (Util.isEmpty(schoolNewsBean.getContent())) {
                headerViewHolder.twitterContent.setVisibility(View.GONE);
            } else {
                headerViewHolder.twitterContent.setVisibility(View.VISIBLE);
                headerViewHolder.twitterContent.setText(schoolNewsBean.getContent());
            }

            buildHeadPic(headerViewHolder.accountAvatar, schoolNewsBean.getHeadUrl(),
                schoolNewsBean.getContentType(), headerViewHolder.accountName,
                schoolNewsBean.getLinkName());

            headerViewHolder.tvTweetTime.setText(
                Util.friendly_time(schoolNewsBean.getCreateTime()));

            schoolNewsBean.setLikeAccountName(headerViewHolder.likeAccountName);

            if (schoolNewsBean.getIsThumbup().equals("1")) {
                headerViewHolder.linearLikeState.setBackgroundResource(
                    R.drawable.comment_border_round_enable);
                headerViewHolder.tvLike.setTextColor(
                    context.getResources().getColor(R.color.white));
                headerViewHolder.btnLike.setImageResource(R.drawable.wutuo_ico_zan_w);
            } else {
                headerViewHolder.linearLikeState.setBackgroundResource(
                    R.drawable.comment_border_round);
                headerViewHolder.tvLike.setTextColor(
                    context.getResources().getColor(R.color.textDarkfive));
                headerViewHolder.btnLike.setImageResource(R.drawable.wutuo_ico_zan);
            }

            //if (schoolNewsBean.getIsTop().equals("0")) {
            //    headerViewHolder.linearSetTop.setBackgroundResource(
            //        R.drawable.comment_border_round);
            //    headerViewHolder.tvSetTop.setTextColor(
            //        context.getResources().getColor(R.color.textDarkfive));
            //} else {
            //    headerViewHolder.linearSetTop.setBackgroundResource(
            //        R.drawable.comment_border_round_enable);
            //    headerViewHolder.tvSetTop.setTextColor(
            //        context.getResources().getColor(R.color.white));
            //}

            headerViewHolder.contentPic.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (schoolNewsBean.getContentShowtype() != null && schoolNewsBean.getContent()
                        .equals("2")) {
                        context.startActivity(new Intent(context, VideoPlayerActivity.class)
                            .putExtra("url", schoolNewsBean.getVideo()));
                    } else {
                        Intent intent = new Intent(context, GalleryActivity.class);
                        intent.putStringArrayListExtra("urls", schoolNewsBean.getImage());
                        context.startActivity(intent);
                    }
                }
            });

            //headerViewHolder.linearSetTop.setOnClickListener(new View.OnClickListener() {
            //    @Override public void onClick(View v) {
            //        int isTop = 0;
            //        if (schoolNewsBean.getIsTop().equals("0")) {
            //            isTop = 1;
            //            headerViewHolder.linearSetTop.setBackgroundResource(
            //                R.drawable.comment_border_round_enable);
            //            headerViewHolder.tvSetTop.setTextColor(
            //                context.getResources().getColor(R.color.white));
            //            schoolNewsBean.setIsTop("0");
            //        } else {
            //            headerViewHolder.linearSetTop.setBackgroundResource(
            //                R.drawable.comment_border_round);
            //            headerViewHolder.tvSetTop.setTextColor(
            //                context.getResources().getColor(R.color.textDarkfive));
            //            schoolNewsBean.setIsTop("0");
            //        }
            //        setTopPresenter.setTop(App.getInstance().getParentId(), schoolNewsBean.getId(),
            //            isTop);
            //    }
            //});
            headerViewHolder.linearLikeState.setOnClickListener(
                getListener(headerViewHolder, schoolNewsBean));
        }
    }

    @Override public void showLoading(String msg) {
        isResult = true;

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override public void showError(int type) {
        if (type == DetailTwitterActivity.RESULT_SUCESS) {
            isResult = true;
            Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
        } else if (type == DetailTwitterActivity.RESULT_ERROR) {
            Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
        }
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView accountName;
        private TextView tvCommentTime;
        private CircularImageViewHome accountAvatar;
        private TextView commentContent;

        public ViewHolder(View itemView) {
            super(itemView);
            accountAvatar = (CircularImageViewHome) itemView.findViewById(R.id.avatar);
            accountName = (TextView) itemView.findViewById(R.id.account_name);
            tvCommentTime = (TextView) itemView.findViewById(R.id.tv_comment_time);
            commentContent = (TextView) itemView.findViewById(R.id.twitter_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onListItemClick.onItemClick(v);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    onListItemLongClick.onItemLongClick(v);
                    return true;
                }
            });
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private CircularImageViewHome accountAvatar;
        private TextView accountName;
        private TextView twitterContent;
        private ImageView contentPic;
        private GridLayout contentPicMulti;
        private TextView tvTweetTime;
        private TextView tvTweetClass;
        //private TextView tvSetTop;
        private TextView tvLike;
        private TextView likeAccountName;
        private LinearLayout linearLikeState;

        private View likeView;
        private ImageView btnLike;
        private ImageView imgIconLike;
        private View view;
        private TextView tvCommentNumber;
        //private LinearLayout linearSetTop;
        private FrameLayout framePic;
        private ImageView imgIconPlay;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            accountAvatar = (CircularImageViewHome) itemView.findViewById(R.id.avatar);
            accountName = (TextView) itemView.findViewById(R.id.account_name);
            twitterContent = (TextView) itemView.findViewById(R.id.twitter_content);
            tvTweetTime = (TextView) itemView.findViewById(R.id.tv_tweet_time);
            tvTweetClass = (TextView) itemView.findViewById(R.id.tv_tweet_class);
            //tvSetTop = (TextView) itemView.findViewById(R.id.tv_setTop);
            tvLike = (TextView) itemView.findViewById(R.id.tv_like);
            contentPicMulti = (GridLayout) itemView.findViewById(R.id.content_pic_multi);
            contentPic = (ImageView) itemView.findViewById(R.id.content_pic);
            likeAccountName = (TextView) itemView.findViewById(R.id.like_account_name);
            linearLikeState = (LinearLayout) itemView.findViewById(R.id.linear_like_state);
            likeView = (View) itemView.findViewById(R.id.like_view);
            btnLike = (ImageView) itemView.findViewById(R.id.btn_like);
            imgIconLike = (ImageView) itemView.findViewById(R.id.img_icon_like);
            view = (View) itemView.findViewById(R.id.view);
            tvCommentNumber = (TextView) itemView.findViewById(R.id.tv_comment_number);
            //linearSetTop = (LinearLayout) itemView.findViewById(R.id.linear_setTop);
            framePic = (FrameLayout) itemView.findViewById(R.id.frame_pic);
            imgIconPlay = (ImageView) itemView.findViewById(R.id.img_icon_play);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("TAG", "onClick");
                    onListItemClick.onItemClick(v);
                }
            });
        }
    }

    class HeaderNotificationHolder extends RecyclerView.ViewHolder {

        public HeaderNotificationHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && getType() == TYPE_NOTIFICATON) {
            return TYPE_NOTIFICATON;
        } else if (position == 0 && getType() == TYPE_HEADER) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return list.size();
        } else {
            return list.size() + 1;
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
                    .centerCrop()
                    .override(Util.dip2px(context, 85), Util.dip2px(context, 85))
                    .into(pic);

                final int position = i;
                pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

    private void buildComeFormClass(TextView tvTweetClass, List<ClassEntity> classSet) {

        StringBuilder strComeClass = new StringBuilder("来自");
        for (ClassEntity str : classSet) {
            strComeClass.append(str.getName());
        }
        tvTweetClass.setText(strComeClass.toString());
    }

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

    private View.OnClickListener getListener(final HeaderViewHolder viewHolder,
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

                    likeOrCommentPresenter.getLikeOrComment(App.getInstance().getParentId(),
                        schoolNewsBean.getId(),
                        "2", App.getInstance().getParentId(), "3",
                        App.getInstance().getParentInfo().getcName() + " " + App.getInstance()
                            .getParentInfo()
                            .getRoleName());
                }
                schoolNewsBean.setLikeAccountName(viewHolder.likeAccountName);
            }
        };
    }
}
