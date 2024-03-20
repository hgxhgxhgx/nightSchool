package com.tencent.wxcloudrun.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.dto.UserInfoDTO;
import com.tencent.wxcloudrun.model.bizDO.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoDO> {

    @Select("SELECT " +
            "id " +
            "FROM user_info  where open_id = #{openId}")
    Long queryIdByOpenId(@Param("openId") String openId);

    @Select("SELECT id,nick_name,phone,career,avatar_url,point,wechat_id,gender " +
    "FROM user_info where open_id = #{openId}")
    UserInfoDO queryUserInfoByOpenId(@Param("openId") String openId);

    @Update("update user_info set user_city = #{userInfoDTO.userCity},user_city_code = #{userInfoDTO.userCityCode},`point` = #{userInfoDTO.point} WHERE id = #{userInfoDTO.id}")
    Integer updateLocateById(@Param("userInfoDTO") UserInfoDTO userInfoDTO);

}
