package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.UserInfoDTO;

public interface UserInfoService {
    public UserInfoDTO getUserInfoByOpenId(String openId);

    public Integer updateLocate(UserInfoDTO userInfoDTO);

    public Integer updateUserInfo(UserInfoDTO userInfoDTO);

    public Integer registerUser(UserInfoDTO userInfoDTO,String openId);

    public Boolean applyCourse(String openId,Long courseId);

    public Boolean cancelCourse(String openId,Long courseId);

    public Boolean favorite(String openId,Long courseId);

    public Boolean unfavorite(String openId,Long courseId);


}
