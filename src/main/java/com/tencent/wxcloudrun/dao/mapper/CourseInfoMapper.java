package com.tencent.wxcloudrun.dao.mapper;

import com.tencent.wxcloudrun.dao.BaseMapper;
import com.tencent.wxcloudrun.model.bizDO.Counter;
import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.jdbc.core.SqlProvider;

import java.util.List;

@Mapper
public interface CourseInfoMapper   extends BaseMapper<CourseInfoDO>{

  @Select("SELECT " +
          "id, gmt_create, gmt_modify, course_name, banner_url, location, POINT, adcode, price, " +
          "period, start_time, end_time, total_num, current_num, status  " +
          "FROM course_info  ORDER BY id DESC  LIMIT #{limit}")
  List<CourseInfoDO> queryByLimit(@Param("limit") Integer limit);

}
