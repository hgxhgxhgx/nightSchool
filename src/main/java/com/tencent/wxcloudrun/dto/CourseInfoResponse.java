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
    //新增距离和报名人数
    String distance;

    //新增是否收藏，是否报名
    Boolean applied;
    Boolean favorited;

    //增加二维码 url
    String extraInfo;




}
