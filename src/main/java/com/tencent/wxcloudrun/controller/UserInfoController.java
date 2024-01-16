package com.tencent.wxcloudrun.controller;


import com.sun.deploy.net.HttpUtils;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;

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
    @GetMapping("/getUserIdByJsCode")
    public ApiResponse getUserIdByJsCode(String JsCode){
        if(StringUtils.isBlank(JsCode)){
            return ApiResponse.error("JsCode不能为空");
        }
        Object res = userInfoService.getUserIdByJsCode(JsCode);
        return ApiResponse.ok(res);
    }
}
