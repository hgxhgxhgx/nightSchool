package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.mysql.cj.Session;
import com.tencent.wxcloudrun.dao.mapper.UserInfoMapper;
import com.tencent.wxcloudrun.dto.UserInfoDTO;
import com.tencent.wxcloudrun.model.bizDO.UserInfoDO;
import com.tencent.wxcloudrun.service.UserInfoService;
import common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public UserInfoDTO getUserIdByJsCode(String JsCode) {


        String openId = getOpenIdByJsCode(JsCode);
        if(StringUtils.isBlank(openId)){
            log.error("用户信息获取失败");
            return null;
        }
        return getUserInfoByOpenId(openId);


    }


    private UserInfoDTO getUserInfoByOpenId(String openId){
        UserInfoDO userInfoDO = userInfoMapper.queryUserInfoByOpenId(openId);
        if(null == userInfoDO){
            return null;
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userInfoDO ,userInfoDTO);
        return userInfoDTO;
    }

    private String getOpenIdByJsCode(String JsCode){
        Map param = new HashMap<String,String>();
        param.put("appid",APP_ID);
        param.put("secret",APP_SECRET);
        param.put("js_code",JsCode);
        param.put("grant_type",GRANT_TYPE);
        try {
            String openIdRes = HttpUtil.doGet(USER_INFO_URL ,param);
            JSONObject openIdObj = JSONObject.parseObject(openIdRes);
            String openId = openIdObj.getString("openid");

            if(StringUtils.isBlank(openId)){
                log.error("获取用户openId为空，result:{}",openIdRes);
                return null;
            }
            log.info("获取用户信息  获取到的openId为:{}",openId);
            return openId;
        }catch (Exception e){
            log.error("获取用户openId失败，失败原因：{}",e.getMessage());
            return null;
        }
    }

    @Override
    public Integer updateLocate(UserInfoDTO userInfoDTO) {
        return userInfoMapper.updateLocateById(userInfoDTO);
    }

    @Override
    public Integer registerUser(UserInfoDTO userInfoDTO, String JsCode) {
        String openId = getOpenIdByJsCode(JsCode);
        if(StringUtils.isBlank(openId)){
            log.error("注册用户 用户信息注册失败，获取的openId为空，userInfo:{},JsCode:{}",userInfoDTO,JsCode);
            return null;
        }
        //已经存在则更新
        UserInfoDO exitUserInfo = userInfoMapper.queryUserInfoByOpenId(openId);
        if(exitUserInfo != null){
            log.info("用户注册 用户已存在，进行更新操作。openId:{}，id:{},name:{}",openId,exitUserInfo.getId(),userInfoDTO.getNickName());
            Long id = exitUserInfo.getId();
            BeanUtils.copyProperties(userInfoDTO, exitUserInfo);
            exitUserInfo.setId(id);
            return userInfoMapper.updateById(exitUserInfo);
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(userInfoDTO,userInfoDO);
        userInfoDO.setOpenId(openId);
        userInfoDO.setUnionId(openId);
        userInfoDO.setId(null);
        return userInfoMapper.insert(userInfoDO);

    }
}
