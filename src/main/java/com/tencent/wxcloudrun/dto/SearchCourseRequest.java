package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.util.StringTokenizer;

@Data
public class SearchCourseRequest {


  private String keyword;

  private Long distance;

  private String dateStart;

  private String dateEnd;

  private String userPoint;

  private Integer page;

  private Integer pageSize;

}
