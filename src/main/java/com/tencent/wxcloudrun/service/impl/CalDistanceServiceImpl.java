package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.wxcloudrun.config.Constants;
import com.tencent.wxcloudrun.dao.mapper.CountersMapper;
import com.tencent.wxcloudrun.dao.mapper.CourseInfoMapper;
import com.tencent.wxcloudrun.dao.mapper.UserInfoMapper;
import com.tencent.wxcloudrun.model.bizDO.Counter;
import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;
import com.tencent.wxcloudrun.model.bizDO.UserInfoDO;
import com.tencent.wxcloudrun.service.CalDistanceService;
import com.tencent.wxcloudrun.service.CounterService;
import com.tencent.wxcloudrun.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CalDistanceServiceImpl implements CalDistanceService {

  @Autowired
  UserInfoMapper userInfoMapper;

  @Autowired
  CourseInfoMapper courseInfoMapper;

  @Override
  public String calDistanceById(String openId, Long courseId) {
    QueryWrapper<UserInfoDO> wrapper = new QueryWrapper<>();
    wrapper.eq(Constants.OPEN_ID, openId);
    UserInfoDO userInfoDO = userInfoMapper.selectOne(wrapper);
    if(userInfoDO == null || userInfoDO.getPoint() == null){
      log.error("calDistance 用户没有位置信息，openId:{},courseId:{}",openId, courseId);
      return null;
    }
    CourseInfoDO courseInfoDO = courseInfoMapper.selectById(courseId);
    if(courseInfoDO == null || courseInfoDO.getPoint() == null){
      log.error("calDistance 课程没有位置信息，openId:{},courseId:{}",openId, courseId);
      return null;
    }
    return MapUtils.calDistance(userInfoDO.getPoint(),courseInfoDO.getPoint());
  }

  @Override
  public String calDistanceByOpenId(String openId, String coursePoint) {
    QueryWrapper<UserInfoDO> wrapper = new QueryWrapper<>();
    wrapper.eq(Constants.OPEN_ID, openId);
    UserInfoDO userInfoDO = userInfoMapper.selectOne(wrapper);
    if(userInfoDO == null || userInfoDO.getPoint() == null){
      log.error("calDistance 用户没有位置信息，openId:{},coursePoint:{}",openId, coursePoint);
      return null;
    }
    return MapUtils.calDistance(userInfoDO.getPoint(),coursePoint);
  }

  @Override
  public String calDistanceByCourseId(String userPoint, Long courseId) {
    CourseInfoDO courseInfoDO = courseInfoMapper.selectById(courseId);
    if(courseInfoDO == null || courseInfoDO.getPoint() == null){
      log.error("calDistance 课程没有位置信息，userPoint:{},courseId:{}",userPoint, courseId);
      return null;
    }
    return MapUtils.calDistance(userPoint, courseInfoDO.getPoint());
  }

  @Override
  public String calDistanceByPoint(String userPoint, String coursePoint) {
    return MapUtils.calDistance(userPoint, coursePoint);
  }
}
