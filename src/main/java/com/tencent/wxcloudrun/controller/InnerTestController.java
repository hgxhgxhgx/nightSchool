package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.UserInfoDTO;
import com.tencent.wxcloudrun.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/innerTest")
public class InnerTestController {


    @Autowired
    UserInfoService userInfoService;


    @GetMapping("/deleteMyInfo")
    public ApiResponse deleteMyInfo(@RequestHeader(name = "x-wx-openid") String openId){
        log.info("deleteMyInfo  openId:{}",openId);
        UserInfoDTO userInfoDTO = userInfoService.getUserInfoByOpenId(openId);
        if(null == userInfoDTO){
            return ApiResponse.ok();
        }
        return ApiResponse.ok(userInfoDTO);
    }
}
