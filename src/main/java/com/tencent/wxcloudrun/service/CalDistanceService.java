package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.bizDO.Counter;

import java.util.Optional;

public interface CalDistanceService {
  public String calDistanceById(String openId,Long courseId);

  public  String calDistanceByOpenId(String openId,String coursePoint);

  public  String calDistanceByCourseId(String userPoint,Long courseId);

  public  String calDistanceByPoint(String userPoint,String coursePoint);

}
