package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CourseInfoResponse;
import com.tencent.wxcloudrun.service.CourseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程控制器
 */
@RestController
@RequestMapping("/course")
@Slf4j
public class CourseController {

  @Autowired
  CourseInfoService courseInfoService;


  /**
   * 分页查询已报名课程
   */
  @GetMapping("/myApply")
  public ApiResponse myApply(@RequestHeader(name = "x-wx-openid") String openId,
                                   Integer pageSize,Integer page) {
    log.info("myApply in,openId:{},pageSize:{},page:{}",openId, pageSize, page);
    Assert.notNull(pageSize,"pageSize不能为空");
    Assert.notNull(page,"page不能为空");
    List<CourseInfoResponse> myApplyByPage = courseInfoService.getMyApplyByPage(openId, pageSize, page);
    return ApiResponse.ok(myApplyByPage);

  }

  @GetMapping("/myFavorite")
  public ApiResponse myFavorite(@RequestHeader(name = "x-wx-openid") String openId,
                             Integer pageSize,Integer page) {
    log.info("myFavorite in,openId:{},pageSize:{},page:{}",openId, pageSize, page);
    Assert.notNull(pageSize,"pageSize不能为空");
    Assert.notNull(page,"page不能为空");
    List<CourseInfoResponse> myFavoriteByPage = courseInfoService.getMyFavoriteByPage(openId, pageSize, page);
    return ApiResponse.ok(myFavoriteByPage);

  }



}
