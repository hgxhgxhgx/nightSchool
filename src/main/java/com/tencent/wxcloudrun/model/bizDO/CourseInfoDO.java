package com.tencent.wxcloudrun.model.bizDO;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tencent.wxcloudrun.model.BaseDO;
import com.tencent.wxcloudrun.model.common.CourseExtraInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "course_info")
public class CourseInfoDO extends BaseDO implements Serializable   {


    String courseName;
    String bannerUrl;
    String location;
    String point;
    String adcode;
    Integer price;
    Integer period;
    Date startTime;
    Date endTime;
    Integer totalNum;
    Integer currentNum;
    String status;
    String detailPhotoUrl;
    String description;
    String content;

    String extraInfo;




}
