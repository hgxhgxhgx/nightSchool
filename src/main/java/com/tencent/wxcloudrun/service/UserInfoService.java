package com.tencent.wxcloudrun.service;

import com.alibaba.fastjson2.JSONObject;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.UserInfoDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface UserInfoService {
    public UserInfoDTO getUserIdByJsCode(String JsCode);

    public Integer updateLocate(UserInfoDTO userInfoDTO);

    public Integer registerUser(UserInfoDTO userInfoDTO,String JsCode);

    public Boolean applyCourse(String openId,Long courseId);

    public Boolean cancelCourse(String openId,Long courseId);

    public Boolean favorite(String openId,Long courseId);

    public Boolean unfavorite(String openId,Long courseId);


}
