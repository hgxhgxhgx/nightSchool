package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.UserInfoDTO;

public interface UserInfoService {
    public UserInfoDTO getUserIdByJsCode(String JsCode);

    public Integer updateLocate(UserInfoDTO userInfoDTO);

    public Integer registerUser(UserInfoDTO userInfoDTO,String JsCode);

}
