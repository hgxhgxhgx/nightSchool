package com.tencent.wxcloudrun.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.dto.UserInfoDTO;
import com.tencent.wxcloudrun.model.bizDO.UserApplyDO;
import com.tencent.wxcloudrun.model.bizDO.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserApplyMapper extends BaseMapper<UserApplyDO> {

    @Select("SELECT " +
            "id " +
            "FROM user_apply  where user_id = #{userId} and course_id = #{courseId}")
    Long queryId(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Select("SELECT " +
            "id " +
            "FROM user_apply  where open_id = #{openId} and course_id = #{courseId}")
    Long queryIdByOpenId(@Param("openId") String openId, @Param("courseId") Long courseId);

    @Select("SELECT id " +
            "FROM user_apply  where user_id = #{userId} and course_id = #{courseId} and status = " +
            "#{status}")
    Long queryIdByStatus(@Param("userId") Long userId,
                              @Param("courseId")  Long courseId, @Param("status") Integer status);

}
