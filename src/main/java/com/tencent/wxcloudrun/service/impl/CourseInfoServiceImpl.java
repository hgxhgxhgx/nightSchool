package com.tencent.wxcloudrun.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.tencent.wxcloudrun.dao.mapper.CourseInfoMapper;
import com.tencent.wxcloudrun.dto.CourseInfoResponse;
import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;
import com.tencent.wxcloudrun.service.CourseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseInfoServiceImpl implements CourseInfoService {

    @Resource
    CourseInfoMapper courseInfoMapper;
    @Override
    public List<CourseInfoResponse> getLimitCourse(Integer limit) {
        List<CourseInfoDO> courseInfoDOS = Optional.ofNullable(courseInfoMapper.queryByLimit(limit)).orElse(new ArrayList<>());
        return courseInfoDOS.stream().map(e -> {
            CourseInfoResponse courseInfoResponse = new CourseInfoResponse();
            BeanUtils.copyProperties(e,courseInfoResponse);
            //格式化时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            courseInfoResponse.setStartTime(simpleDateFormat.format(e.getStartTime()));
            courseInfoResponse.setEndTime(simpleDateFormat.format(e.getEndTime()));
            return courseInfoResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public CourseInfoResponse getCourseById(Long id) {
        CourseInfoDO courseInfoDO = courseInfoMapper.selectById(id);
        CourseInfoResponse result = new CourseInfoResponse();
        BeanUtils.copyProperties(courseInfoDO, result);
        //格式化时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        result.setStartTime(simpleDateFormat.format(courseInfoDO.getStartTime()));
        result.setEndTime(simpleDateFormat.format(courseInfoDO.getEndTime()));
        return result;
    }
}
