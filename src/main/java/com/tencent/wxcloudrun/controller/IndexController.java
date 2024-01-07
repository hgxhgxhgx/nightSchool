package com.tencent.wxcloudrun.controller;

import com.alibaba.fastjson2.JSONObject;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CourseInfoResponse;
import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;
import com.tencent.wxcloudrun.service.CourseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * index控制器
 */
@RestController
@RequestMapping("/index")
@Slf4j
public class IndexController {

  @Autowired
  CourseInfoService courseInfoService;

  /**
   * 主页页面
   * @return API response html
   */
  @GetMapping("/getRecommendCourse")
  public ApiResponse getRecommendCourse() {
    log.info("getRecommendCourse in");
    List<CourseInfoResponse> limitCourse = courseInfoService.getLimitCourse(10);
    log.info("getRecommendCourse out");
    return ApiResponse.ok(limitCourse);

  }

  @GetMapping("/getCourseById")
  public ApiResponse getCourseById(Long id) {
    log.info("getCourseById in");
    CourseInfoResponse course = courseInfoService.getCourseById(id);
    return ApiResponse.ok(course);

  }

}
