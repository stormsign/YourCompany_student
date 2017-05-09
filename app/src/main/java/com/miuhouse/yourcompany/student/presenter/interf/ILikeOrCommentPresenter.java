package com.miuhouse.yourcompany.student.presenter.interf;

import com.miuhouse.yourcompany.student.model.CommentsBean;

/**
 * Created by kings on 12/30/2016.
 */
public interface ILikeOrCommentPresenter {
  void getLikeOrComment(String fId, String schoolTeacherId, String newsId, String commentType,
                        String linkId,
                        String linkType, String linkName, String linkedId, String linkedType, String linkedName,
                        String comment,CommentsBean mCommentsBean);

  void getLikeOrComment(String schoolTeacherId, String newsId, String commentType,
                        String linkId,
                        String linkType, String linkName
  );

  void cancelLike(String schoolTeacherId, String schoolNewsId);
}
