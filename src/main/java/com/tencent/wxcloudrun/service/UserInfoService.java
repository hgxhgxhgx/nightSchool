package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.UserInfoDTO;

public interface UserInfoService {
    public UserInfoDTO getUserIdByJsCode(String JsCode);
}
