package com.tencent.wxcloudrun.utils;

import com.tencent.wxcloudrun.dao.mapper.UserApplyMapper;
import com.tencent.wxcloudrun.dao.mapper.UserFavoriteMapper;
import com.tencent.wxcloudrun.dao.mapper.UserInfoMapper;
import com.tencent.wxcloudrun.model.enumList.UserApplyStatusEnum;
import com.tencent.wxcloudrun.model.enumList.UserFavoriteStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCourseRelationUtils {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserApplyMapper userApplyMapper;

    @Autowired
    UserFavoriteMapper userFavoriteMapper;

    public  Boolean isApplied(String openId,Long courseId){

        Long userId = userInfoMapper.queryIdByOpenId(openId);
        if(userId != null){
            Long applyId = userApplyMapper.queryIdByStatus(userId, courseId,
                    UserApplyStatusEnum.apply.getCode());

            if(applyId != null) {
                return true;
            }
        }
        return false;
    }

    public  Boolean isFavorited(String openId,Long courseId){

        Long userId = userInfoMapper.queryIdByOpenId(openId);
        if(userId != null){
            Long Id = userFavoriteMapper.queryIdByStatus(userId, courseId,
                    UserFavoriteStatusEnum.favorite.getCode());

            if(Id != null) {
                return true;
            }
        }
        return false;
    }


}
