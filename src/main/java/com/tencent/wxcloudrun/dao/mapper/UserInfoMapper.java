package com.tencent.wxcloudrun.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.model.bizDO.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoDO> {

    @Select("SELECT " +
            "id " +
            "FROM user_info  where open_id = #{openId}")
    Long queryIdByOpenId(@Param("openId") String openId);

    @Select("SELECT id,nick_name,phone,career,avatar_url " +
    "FROM user_info where open_id = #{openId}")
    UserInfoDO queryUserInfoByOpenId(@Param("openId") String openId);

}
