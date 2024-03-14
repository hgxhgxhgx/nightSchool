package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.mysql.cj.Session;
import com.tencent.wxcloudrun.dao.mapper.CourseInfoMapper;
import com.tencent.wxcloudrun.dao.mapper.UserApplyMapper;
import com.tencent.wxcloudrun.dao.mapper.UserFavoriteMapper;
import com.tencent.wxcloudrun.dao.mapper.UserInfoMapper;
import com.tencent.wxcloudrun.dto.UserInfoDTO;
import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;
import com.tencent.wxcloudrun.model.bizDO.UserApplyDO;
import com.tencent.wxcloudrun.model.bizDO.UserFavoriteDO;
import com.tencent.wxcloudrun.model.bizDO.UserInfoDO;
import com.tencent.wxcloudrun.model.enumList.UserApplyStatusEnum;
import com.tencent.wxcloudrun.model.enumList.UserFavoriteStatusEnum;
import com.tencent.wxcloudrun.service.UserInfoService;
import common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    String USER_INFO_URL = "https://api.weixin.qq.com/sns/jscode2session";
    String APP_ID = "wx8f9e509ee505e487";
    String APP_SECRET = "564f985c4d6852017290cde4e2329102";

    String GRANT_TYPE = "authorization_code";
    
    public static final String USER_ID = "user_id";

    public static final String COURSE_ID = "course_id";

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    CourseInfoMapper courseInfoMapper;

    @Autowired
    UserApplyMapper userApplyMapper;

    @Autowired
    UserFavoriteMapper userFavoriteMapper;


    public UserInfoDTO getUserInfoByOpenId(String openId){
        UserInfoDO userInfoDO = userInfoMapper.queryUserInfoByOpenId(openId);
        if(null == userInfoDO){
            return null;
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userInfoDO ,userInfoDTO);
        return userInfoDTO;
    }

    private String getOpenIdByJsCode(String JsCode){
        Map param = new HashMap<String,String>();
        param.put("appid",APP_ID);
        param.put("secret",APP_SECRET);
        param.put("js_code",JsCode);
        param.put("grant_type",GRANT_TYPE);
        try {
            String openIdRes = HttpUtil.doGet(USER_INFO_URL ,param);
            JSONObject openIdObj = JSONObject.parseObject(openIdRes);
            String openId = openIdObj.getString("openid");

            if(StringUtils.isBlank(openId)){
                log.error("获取用户openId为空，result:{}",openIdRes);
                return null;
            }
            log.info("获取用户信息  获取到的openId为:{}",openId);
            return openId;
        }catch (Exception e){
            log.error("获取用户openId失败，失败原因：{}",e.getMessage());
            return null;
        }
    }

    @Override
    public Integer updateLocate(UserInfoDTO userInfoDTO) {
        return userInfoMapper.updateLocateById(userInfoDTO);
    }

    @Override
    public Integer updateUserInfo(UserInfoDTO userInfoDTO) {
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(userInfoDTO, userInfoDO);
        return userInfoMapper.updateById(userInfoDO);
    }

    @Override
    public Integer registerUser(UserInfoDTO userInfoDTO, String openId) {
//        String openId = getOpenIdByJsCode(JsCode);
//        if(StringUtils.isBlank(openId)){
//            log.error("注册用户 用户信息注册失败，获取的openId为空，userInfo:{},JsCode:{}",userInfoDTO,JsCode);
//            return null;
//        }
        //已经存在则更新
        UserInfoDO exitUserInfo = userInfoMapper.queryUserInfoByOpenId(openId);
        if(exitUserInfo != null){
            log.info("用户注册 用户已存在，进行更新操作。openId:{}，id:{},name:{}",openId,exitUserInfo.getId(),userInfoDTO.getNickName());
            Long id = exitUserInfo.getId();
            BeanUtils.copyProperties(userInfoDTO, exitUserInfo);
            exitUserInfo.setId(id);
            return userInfoMapper.updateById(exitUserInfo);
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(userInfoDTO,userInfoDO);
        userInfoDO.setOpenId(openId);
        userInfoDO.setUnionId(openId);
        userInfoDO.setId(null);
        return userInfoMapper.insert(userInfoDO);

    }


    private Long getUserIdByOpenId(String openId){
        return userInfoMapper.queryIdByOpenId(openId);

    }

    @Override
    public Boolean applyCourse(String openId, Long courseId) {
        Long userId = getUserIdByOpenId(openId);
        if(null == userId){
            log.error("openId:{}用户未注册，courseId:{}",openId,courseId);
            return false;
        }
        //查询课程是否存在
        CourseInfoDO courseInfoDO = courseInfoMapper.selectById(courseId);
        if(null == courseInfoDO){
            log.error("openId:{}，courseId:{} 课程不存在",openId, courseId);
            return false;
        }


        //Long applyId = userApplyMapper.queryApplyId(userId, courseId);
        HashMap<String, Object> param = new HashMap<>();
        param.put(USER_ID, userId);
        param.put(COURSE_ID, courseId);
        List<UserApplyDO> userApplyDOS = userApplyMapper.selectByMap(param);
        if(CollectionUtils.isEmpty(userApplyDOS)){
            //插入一条新记录
            UserApplyDO userApplyDO = new UserApplyDO();
            userApplyDO.setUserId(userId);
            userApplyDO.setCourseId(courseId);
            userApplyDO.setOpenId(openId);
            userApplyDO.setStatus(UserApplyStatusEnum.apply.getCode());
            userApplyMapper.insert(userApplyDO);
            //todo 课程人数+1 这里以后要做并发 加redis锁
            courseInfoDO.setCurrentNum(courseInfoDO.getCurrentNum()+1);
            courseInfoMapper.updateById(courseInfoDO);
        }else {
            //更新记录状态
            UserApplyDO userApplyDO = userApplyDOS.get(0);
            if(Objects.equals(UserApplyStatusEnum.apply.getCode(),userApplyDO.getStatus())){
                return true;
            }
            userApplyDO.setStatus(UserApplyStatusEnum.apply.getCode());
            userApplyMapper.updateById(userApplyDO);
            //todo 课程人数+1 这里以后要做并发 加redis锁
            courseInfoDO.setCurrentNum(courseInfoDO.getCurrentNum()+1);
            courseInfoMapper.updateById(courseInfoDO);
        }

        return true;
    }

    @Override
    public Boolean cancelCourse(String openId, Long courseId) {
        Long userId = getUserIdByOpenId(openId);
        if(null == userId){
            log.error("openId:{}用户未注册，courseId:{}",openId,courseId);
            return false;
        }
        //查询课程是否存在
        CourseInfoDO courseInfoDO = courseInfoMapper.selectById(courseId);
        if(null == courseInfoDO){
            log.error("openId:{}，courseId:{} 课程不存在",openId, courseId);
            return false;
        }

        //Long applyId = userApplyMapper.queryApplyId(userId, courseId);
        HashMap<String, Object> param = new HashMap<>();
        param.put(USER_ID, userId);
        param.put(COURSE_ID, courseId);
        List<UserApplyDO> userApplyDOS = userApplyMapper.selectByMap(param);
        if(CollectionUtils.isEmpty(userApplyDOS)){
            //不存在记录
            log.info("要取消的课程不存在,userId:{},courseId:{}",userId,courseId);
            return true;
        }else {
            //更新记录状态
            UserApplyDO userApplyDO = userApplyDOS.get(0);
            if(Objects.equals(UserApplyStatusEnum.cancelApply.getCode(),userApplyDO.getStatus())){
                return true;
            }
            userApplyDO.setStatus(UserApplyStatusEnum.cancelApply.getCode());
            userApplyMapper.updateById(userApplyDO);
            //todo 课程人数-1 这里以后要做并发 加redis锁
            courseInfoDO.setCurrentNum(courseInfoDO.getCurrentNum()-1);
            courseInfoMapper.updateById(courseInfoDO);
        }

        return true;
    }

    @Override
    public Boolean favorite(String openId, Long courseId) {
        Long userId = getUserIdByOpenId(openId);
        if(null == userId){
            log.error("openId:{}用户未注册，courseId:{}",openId,courseId);
            return false;
        }
        //Long applyId = userApplyMapper.queryApplyId(userId, courseId);
        HashMap<String, Object> param = new HashMap<>();
        param.put(USER_ID, userId);
        param.put(COURSE_ID, courseId);
        List<UserFavoriteDO> userFavoriteDOS = userFavoriteMapper.selectByMap(param);
        if(CollectionUtils.isEmpty(userFavoriteDOS)){
            //没有记录，新增
            UserFavoriteDO userFavoriteDO = new UserFavoriteDO();
            userFavoriteDO.setUserId(userId);
            userFavoriteDO.setCourseId(courseId);
            userFavoriteDO.setOpenId(openId);
            userFavoriteDO.setStatus(UserFavoriteStatusEnum.favorite.getCode());
            userFavoriteMapper.insert(userFavoriteDO);

        }else {
            //更新记录状态
            UserFavoriteDO userFavoriteDO = userFavoriteDOS.get(0);
            if(Objects.equals(UserFavoriteStatusEnum.favorite.getCode(),userFavoriteDO.getStatus())){
                return true;
            }
            userFavoriteDO.setStatus(UserFavoriteStatusEnum.favorite.getCode());
            userFavoriteMapper.updateById(userFavoriteDO);
        }

        return true;
    }

    @Override
    public Boolean unfavorite(String openId, Long courseId) {
        Long userId = getUserIdByOpenId(openId);
        if(null == userId){
            log.error("openId:{}用户未注册，courseId:{}",openId,courseId);
            return false;
        }
        //Long applyId = userApplyMapper.queryApplyId(userId, courseId);
        HashMap<String, Object> param = new HashMap<>();
        param.put(USER_ID, userId);
        param.put(COURSE_ID, courseId);
        List<UserFavoriteDO> userFavoriteDOS = userFavoriteMapper.selectByMap(param);
        if(CollectionUtils.isEmpty(userFavoriteDOS)){
            //不存在记录
            log.info("要取消收藏的课程不存在,userId:{},courseId:{}",userId,courseId);
            return true;

        }else {
            //更新记录状态
            UserFavoriteDO userFavoriteDO = userFavoriteDOS.get(0);
            if(Objects.equals(UserFavoriteStatusEnum.cancelFavorite.getCode(),userFavoriteDO.getStatus())){
                return true;
            }
            userFavoriteDO.setStatus(UserFavoriteStatusEnum.cancelFavorite.getCode());
            userFavoriteMapper.updateById(userFavoriteDO);
        }

        return true;
    }
}
