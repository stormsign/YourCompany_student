package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IClass;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.ClassEntity;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2017/1/10.
 */
public class ClassInteractor implements IClass {

    @Override
    public void getAllClasses(String parentId, String schoolId,
                              Response.Listener listener, Response.ErrorListener errorListener) {
        String url = Constants.URL_VALUE + "schoolClassList";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", parentId);
        map.put("schoolId", schoolId);
        VolleyManager.getInstance().sendGsonRequest(null, url, map,
                SPUtils.getData(SPUtils.TOKEN, null),
                ClassListBean.class, listener, errorListener);
    }



    public class ClassListBean extends BaseBean {
        List<ClassEntity> schoolClasses;

        public List<ClassEntity> getSchoolClasses() {
            return schoolClasses;
        }

        public void setSchoolClasses(List<ClassEntity> schoolClasses) {
            this.schoolClasses = schoolClasses;
        }
    }
}
