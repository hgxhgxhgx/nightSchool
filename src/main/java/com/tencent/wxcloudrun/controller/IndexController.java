package com.tencent.wxcloudrun.controller;

import com.alibaba.fastjson2.JSONObject;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CourseInfoResponse;
import com.tencent.wxcloudrun.dto.SearchCourseRequest;
import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;
import com.tencent.wxcloudrun.service.CourseInfoService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.sf.jsqlparser.parser.feature.Feature.limit;

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
   * 主页推荐课程 先根据距离来推荐
   * @return API response html
   */
  @GetMapping("/getRecommendCourse")
  public ApiResponse getRecommendCourse(@RequestHeader(name = "x-wx-openid") String openId,
                                        @RequestParam(value = "userPoint",required = false) String userPoint,
                                        @RequestParam(value = "pageSize",required = false,
                                                defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "page",required = false,
                                                defaultValue = "1") Integer page) {
    log.info("getRecommendCourse in,openId:{},userPoint:{},page:{},pageSize:{}",openId,userPoint,
            page,pageSize);
    List<CourseInfoResponse> recommendCourse = courseInfoService.getRecommendCourse(openId, userPoint
            ,pageSize,page);
    return ApiResponse.ok(recommendCourse);

  }

  @GetMapping("/getCourseById")
  public ApiResponse getCourseById(@RequestHeader(name = "x-wx-openid") String openId,Long id) {
    log.info("getCourseById in,openId:{},courseId:{}",openId, id);
    CourseInfoResponse course = courseInfoService.getCourseById(openId, id);
    return ApiResponse.ok(course);

  }

  /**
   * 随机获取指定个数的课程
   * @return API response html
   */
  @GetMapping("/getLimitCourse")
  public ApiResponse getLimitCourse(Integer limit) {
    log.info("getLimitCourse in");
    List<CourseInfoResponse> limitCourse = courseInfoService.getLimitCourse(limit);
    log.info("getLimitCourse out");
    return ApiResponse.ok(limitCourse);

  }

  /**
   * 根据条件搜索课程
   * @return API response html
   */
  @PostMapping("/searchCourse")
  public ApiResponse searchCourse(@RequestHeader(name = "x-wx-openid") String openId,
                                  @RequestBody SearchCourseRequest request) {
    log.info("searchCourse in,request:{}",request);
    List<CourseInfoResponse> searchRes;
    if(request == null || (request.getDistance() == null && request.getDateStart() == null && request.getDateEnd() == null && request.getKeyword() == null)){
      searchRes = courseInfoService.getRecommendCourse(openId,request.getUserPoint(),10,1);
    }else {
      searchRes = courseInfoService.searchCourseByCondition(openId, request);
    }
    return ApiResponse.ok(searchRes);

  }



}
