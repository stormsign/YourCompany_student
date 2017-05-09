package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IMsgInteractor;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.CommentsBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2017/1/17.
 */
public class MsgInteractor implements IMsgInteractor {
    @Override
    public void getUnreadMsgs(String schoolTeacherId, int page,
                              Response.Listener listener, Response.ErrorListener errorListener) {
        String url = Constants.URL_VALUE + "schoolCommentList";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", schoolTeacherId);
        map.put("page", page);
        map.put("pageSize", 20);
        VolleyManager.getInstance().sendGsonRequest(null, url, map,
                SPUtils.getData(SPUtils.TOKEN, null), CommentsBeanList.class,
                listener, errorListener);
    }

    public class CommentsBeanList extends BaseBean {
        List<CommentsBean> schoolComments;

        public List<CommentsBean> getSchoolComments() {
            return schoolComments;
        }

        public void setSchoolComments(List<CommentsBean> schoolComments) {
            this.schoolComments = schoolComments;
        }
    }
}
