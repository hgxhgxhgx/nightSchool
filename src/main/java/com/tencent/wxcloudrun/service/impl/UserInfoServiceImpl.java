package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.service.UserInfoService;
import common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    String USER_INFO_URL = "https://api.weixin.qq.com/sns/jscode2session";
    String APP_ID = "wx8f9e509ee505e487";
    String APP_SECRET = "564f985c4d6852017290cde4e2329102";

    String GRANT_TYPE = "authorization_code";

    @Override
    public Object getUserIdByJsCode(String JsCode) {
        Map param = new HashMap<String,String>();
        param.put("appid",APP_ID);
        param.put("secret",APP_SECRET);
        param.put("js_code",JsCode);
        param.put("grant_type",GRANT_TYPE);
        try {
            String userInfoStr = HttpUtil.doGet(USER_INFO_URL,param);
            return userInfoStr;
        } catch (Exception e){
            log.error("获取用户信息失败，e:{}",e.getMessage());
            return  -1L;
        }

    }
}
