package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.mapper.CourseInfoMapper;
import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;
import com.tencent.wxcloudrun.service.CourseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CourseInfoServiceImpl implements CourseInfoService {

    @Resource
    CourseInfoMapper courseInfoMapper;
    @Override
    public List<CourseInfoDO> getLimitCourse(Integer limit) {
        return courseInfoMapper.queryByLimit(limit);
    }
}
