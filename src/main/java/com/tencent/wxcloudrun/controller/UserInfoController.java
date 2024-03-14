package com.tencent.wxcloudrun.controller;



import com.alibaba.fastjson2.JSONObject;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.UserInfoDTO;
import com.tencent.wxcloudrun.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.TimeZone;


@RestController
@Slf4j
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;
    /**
     * 通过wx.login获取用户在后端注册的id，若没有注册则返回-1
     * @return
     */
    @GetMapping("/getUserInfoByJsCode")
    public ApiResponse getUserInfoByJsCode(@RequestHeader(name = "x-wx-openid") String openId,
                                           String JsCode){
        UserInfoDTO userInfoDTO = userInfoService.getUserInfoByOpenId(openId);
        if(null == userInfoDTO){
            return ApiResponse.ok();
        }
        return ApiResponse.ok(userInfoDTO);
    }

    @PostMapping("/updateLocate")
    public ApiResponse updateLocate(@RequestBody UserInfoDTO userInfo){
        log.info("updateLocate userInfo:{}",userInfo);
        if(null == userInfo || null == userInfo.getId() || userInfo.getPoint() == null){
            log.error("updateLocate error, id is null, userInfo:{}",userInfo);
            return ApiResponse.error("更新位置失败，请检查参数！" + userInfo);
        }
        Integer count = userInfoService.updateLocate(userInfo);
        if(count == 0){
            log.error("updateLocate error,更新成功0个，userInfo:{}",userInfo);
            return ApiResponse.error("请检查用户id为["+ userInfo.getId()+"]是否存在");
        }
        return ApiResponse.ok();
    }

    @PostMapping("/updateUserInfo")
    public ApiResponse updateUserInfo(@RequestBody UserInfoDTO userInfo){
        log.info("updateUserInfo userInfo:{}",userInfo);
        if(null == userInfo || null == userInfo.getId() ){
            log.error("updateUserInfo error, id is null, userInfo:{}",userInfo);
            return ApiResponse.error("更新用户信息失败，请检查参数！" + userInfo);
        }
        Integer count = userInfoService.updateUserInfo(userInfo);
        if(count == 0){
            log.error("updateUserInfo error,更新成功0个，userInfo:{}",userInfo);
            return ApiResponse.error("请检查用户id为["+ userInfo.getId()+"]是否存在");
        }
        return ApiResponse.ok();
    }
    @PostMapping("/registerUser")
    public ApiResponse registerUser(@RequestHeader(name = "x-wx-openid") String openId,@RequestBody JSONObject request){
        log.info("用户注册 入参：{}，openId:{}",request, openId);
        String userInfo = request.getString("userInfo");
        //String jsCode = request.getString("JsCode");
        Assert.notNull(userInfo,"用户数据为空");
        //Assert.notNull(jsCode,"jsCode为空");
        UserInfoDTO userInfoDTO = JSONObject.parseObject(userInfo, UserInfoDTO.class);
        Integer res = userInfoService.registerUser(userInfoDTO, openId);
        if(null == res || 0 == res){
            log.error("用户信息注册失败，request：{}",request);
            return ApiResponse.error("用户注册失败");
        }
        return ApiResponse.ok("用户注册成功");


    }

    @PostMapping("/timeZone")
    public ApiResponse timeZone(@RequestHeader(name = "x-wx-openid") String openId,@RequestBody JSONObject body){
        log.info("openId:{}",openId);
        log.info("body:{}",body);
        log.info("获取时区");
        TimeZone aDefault = TimeZone.getDefault();
        log.info("当前时间：{}",new Date());
        log.info("获取到到时区:{}",aDefault);

        return null;
    }


    @PostMapping("/applyCourse")
    public ApiResponse applyCourse(@RequestHeader(name = "x-wx-openid") String openId,@RequestBody JSONObject body){
        log.info("applyCourse,openId:{},body:{}",openId,body);
        if(StringUtils.isBlank(openId)){
            return ApiResponse.error("用户openId为空");
        }
        if(body == null){
            return ApiResponse.error("入参格式错误");
        }
        Long courseId = body.getLong("courseId");
        if(null == courseId){
            return ApiResponse.error("courseId为空");
        }
        return ApiResponse.ok(userInfoService.applyCourse(openId, courseId));

    }

    @PostMapping("/cancelCourse")
    public ApiResponse cancelCourse(@RequestHeader(name = "x-wx-openid") String openId,@RequestBody JSONObject body){
        log.info("cancelCourse,openId:{},body:{}",openId,body);
        if(StringUtils.isBlank(openId)){
            return ApiResponse.error("用户openId为空");
        }
        if(body == null){
            return ApiResponse.error("入参格式错误");
        }
        Long courseId = body.getLong("courseId");
        if(null == courseId){
            return ApiResponse.error("courseId为空");
        }
        return ApiResponse.ok(userInfoService.cancelCourse(openId, courseId));
    }

    @PostMapping("/favorite")
    public ApiResponse favorite(@RequestHeader(name = "x-wx-openid") String openId,@RequestBody JSONObject body){
        log.info("favorite,openId:{},body:{}",openId,body);
        if(StringUtils.isBlank(openId)){
            return ApiResponse.error("用户openId为空");
        }
        if(body == null){
            return ApiResponse.error("入参格式错误");
        }
        Long courseId = body.getLong("courseId");
        if(null == courseId){
            return ApiResponse.error("courseId为空");
        }
        return ApiResponse.ok(userInfoService.favorite(openId, courseId));
    }


    @PostMapping("/unFavorite")
    public ApiResponse unfavorite(@RequestHeader(name = "x-wx-openid") String openId,@RequestBody JSONObject body){
        log.info("unfavorite,openId:{},body:{}",openId,body);
        if(StringUtils.isBlank(openId)){
            return ApiResponse.error("用户openId为空");
        }
        if(body == null){
            return ApiResponse.error("入参格式错误");
        }
        Long courseId = body.getLong("courseId");
        if(null == courseId){
            return ApiResponse.error("courseId为空");
        }
        return ApiResponse.ok(userInfoService.unfavorite(openId, courseId));
    }
}
