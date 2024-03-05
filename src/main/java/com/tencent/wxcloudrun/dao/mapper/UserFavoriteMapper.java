package com.tencent.wxcloudrun.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.model.bizDO.UserApplyDO;
import com.tencent.wxcloudrun.model.bizDO.UserFavoriteDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavoriteDO> {


    @Select("SELECT id " +
            "FROM user_favorite  where user_id = #{userId} and course_id = #{courseId} and status" +
            " = " +
            "#{status}")
    Long queryIdByStatus(@Param("userId") Long userId,
                              @Param("courseId")  Long courseId, @Param("status") Integer status);

    @Select("SELECT " +
            "id " +
            "FROM user_favorite  where open_id = #{openId} and course_id = #{courseId}")
    Long queryIdByOpenId(@Param("openId") String openId, @Param("courseId") Long courseId);
}
