package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;

import java.util.List;

public interface CourseInfoService {
    public List<CourseInfoDO> getLimitCourse(Integer limit);

}
