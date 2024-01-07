package com.tencent.wxcloudrun.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tencent.wxcloudrun.model.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
public class CourseInfoResponse  {

    Long id;
    String courseName;
    String bannerUrl;
    String location;
    String point;
    String adcode;
    Integer price;
    Integer period;
    String startTime;
    String endTime;
    Integer totalNum;
    Integer currentNum;
    String status;
    String detailPhotoUrl;
    String description;
    String content;


}
