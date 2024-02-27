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
import java.util.Map;
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
    public ApiResponse getUserInfoByJsCode(String JsCode){
        if(StringUtils.isBlank(JsCode)){
            return ApiResponse.error("JsCode不能为空");
        }
        UserInfoDTO userInfoDTO = userInfoService.getUserIdByJsCode(JsCode);
        if(null == userInfoDTO){
            return ApiResponse.ok();
        }
        return ApiResponse.ok(userInfoDTO);
    }

    @PostMapping("/updateLocate")
    public ApiResponse updateLocate(@RequestBody UserInfoDTO userInfo){
        log.info("updateLocate userInfo:{}",userInfo);
        if(null == userInfo || null == userInfo.getId()){
            log.error("updateLocate error, id is null, userInfo:{}",userInfo);
            return ApiResponse.error("更新位置失败，请检查参数！");
        }
        Integer count = userInfoService.updateLocate(userInfo);
        if(count == 0){
            log.error("updateLocate error,更新成功0个，userInfo:{}",userInfo);
            return ApiResponse.error("请检查用户id为["+ userInfo.getId()+"]是否存在");
        }
        return ApiResponse.ok();
    }
    @PostMapping("/registerUser")
    public ApiResponse registerUser(@RequestBody JSONObject request){
        log.info("用户注册 入参：{}",request);
        String userInfo = request.getString("userInfo");
        String jsCode = request.getString("JsCode");
        Assert.notNull(userInfo,"用户数据为空");
        Assert.notNull(jsCode,"jsCode为空");
        UserInfoDTO userInfoDTO = JSONObject.parseObject(userInfo, UserInfoDTO.class);
        Integer res = userInfoService.registerUser(userInfoDTO, jsCode);
        if(null == res || 0 == res){
            log.error("用户信息注册失败，request：{}",request);
            return ApiResponse.error("用户注册失败");
        }
        return ApiResponse.ok("用户注册成功");


    }

    @GetMapping("/timeZone")
    public ApiResponse timeZone(@RequestHeader(name = "x-wx-openid") String openId,@RequestBody JSONObject body){
        log.info("openId:{}",openId);
        log.info("body:{}",body);
        log.info("获取时区");
        TimeZone aDefault = TimeZone.getDefault();
        log.info("当前时间：{}",new Date());
        log.info("获取到到时区:{}",aDefault);

        return null;
    }
}
