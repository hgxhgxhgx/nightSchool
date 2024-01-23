package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    Long id;

    String location;

    String nickName;

    String phone;

    String career;

    String avatarUrl;

    String userCity;

    String userCityCode;

    String point;
}
