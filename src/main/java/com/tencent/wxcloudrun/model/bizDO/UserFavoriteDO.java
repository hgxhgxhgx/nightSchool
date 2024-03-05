package com.tencent.wxcloudrun.model.bizDO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tencent.wxcloudrun.model.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "user_favorite")
@FieldNameConstants
public class UserFavoriteDO extends BaseDO implements Serializable {
    Long userId;

    Long courseId;

    //不做删除操作，仅根据状态判断是报名还是取消报名
    Integer status;

    String openId;
}
