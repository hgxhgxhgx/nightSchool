package com.tencent.wxcloudrun.controller;



import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.UserInfoDTO;
import com.tencent.wxcloudrun.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


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
}
