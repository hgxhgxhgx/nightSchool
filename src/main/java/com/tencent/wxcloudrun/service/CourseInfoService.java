package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.CourseInfoResponse;
import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;

import java.util.List;

public interface CourseInfoService {
    public List<CourseInfoResponse> getLimitCourse(Integer limit);

    public List<CourseInfoResponse> getRecommendCourse(String openId);

    public CourseInfoResponse getCourseById(String openId, Long id);

    public List<CourseInfoResponse> getMyApplyByPage(String openId,Integer pageSize,Integer page);

    public List<CourseInfoResponse> getMyFavoriteByPage(String openId,Integer pageSize,
                                                        Integer page);

}
