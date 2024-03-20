package com.tencent.wxcloudrun.model.bizDO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tencent.wxcloudrun.model.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "user_info")
public class UserInfoDO extends BaseDO implements Serializable {
    String openId;

    String unionId;

    String location;

    String nickName;

    String phone;

    String career;

    String avatarUrl;

    String userCity;

    String userCityCode;

    String point;

    String wechatId;

    //女：0  男：1
    Integer gender;

}
