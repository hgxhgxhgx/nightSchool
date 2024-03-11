package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.config.Constants;
import com.tencent.wxcloudrun.dao.mapper.CourseInfoMapper;
import com.tencent.wxcloudrun.dao.mapper.UserApplyMapper;
import com.tencent.wxcloudrun.dao.mapper.UserFavoriteMapper;
import com.tencent.wxcloudrun.dao.mapper.UserInfoMapper;
import com.tencent.wxcloudrun.dto.CourseInfoResponse;
import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;
import com.tencent.wxcloudrun.model.bizDO.UserApplyDO;
import com.tencent.wxcloudrun.model.bizDO.UserFavoriteDO;
import com.tencent.wxcloudrun.model.enumList.UserApplyStatusEnum;
import com.tencent.wxcloudrun.model.enumList.UserFavoriteStatusEnum;
import com.tencent.wxcloudrun.service.CalDistanceService;
import com.tencent.wxcloudrun.service.CourseInfoService;
import com.tencent.wxcloudrun.utils.UserCourseRelationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseInfoServiceImpl implements CourseInfoService {

    @Resource
    CourseInfoMapper courseInfoMapper;

    @Resource
    UserApplyMapper userApplyMapper;

    @Resource
    UserFavoriteMapper userFavoriteMapper;

    @Resource
    UserInfoMapper userInfoMapper;

    @Resource
    UserCourseRelationUtils userCourseRelationUtils;

    @Resource
    CalDistanceService calDistanceService;

    @Override
    public List<CourseInfoResponse> getLimitCourse(Integer limit) {
        List<CourseInfoDO> courseInfoDOS = Optional.ofNullable(courseInfoMapper.queryByLimit(limit)).orElse(new ArrayList<>());
        return courseInfoDOS.stream().map(e -> {
            CourseInfoResponse courseInfoResponse = new CourseInfoResponse();
            BeanUtils.copyProperties(e,courseInfoResponse);
            //格式化时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            courseInfoResponse.setStartTime(simpleDateFormat.format(e.getStartTime()));
            courseInfoResponse.setEndTime(simpleDateFormat.format(e.getEndTime()));
            return courseInfoResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CourseInfoResponse> getRecommendCourse(String openId) {
        return null;
    }

    @Override
    public CourseInfoResponse getCourseById(String openId, Long id) {
        CourseInfoDO courseInfoDO = courseInfoMapper.selectById(id);
        CourseInfoResponse result = courseInfoToRes(courseInfoDO);
        result.setDistance(calDistanceService.calDistanceByOpenId(openId, courseInfoDO.getPoint()));
        result.setApplied(userCourseRelationUtils.isApplied(openId,id));
        result.setFavorited(userCourseRelationUtils.isFavorited(openId,id));
        return result;
    }

    private CourseInfoResponse courseInfoToRes(CourseInfoDO courseInfoDO){
        CourseInfoResponse result = new CourseInfoResponse();
        BeanUtils.copyProperties(courseInfoDO, result);

        //格式化时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        result.setStartTime(simpleDateFormat.format(courseInfoDO.getStartTime()));
        result.setEndTime(simpleDateFormat.format(courseInfoDO.getEndTime()));
        return result;
    }

    @Override
    public List<CourseInfoResponse> getMyApplyByPage(String openId, Integer pageSize,
                                                     Integer page) {
        Page<UserApplyDO> pageof = Page.of(page,pageSize);
        QueryWrapper<UserApplyDO> wrapper = new QueryWrapper<>();
        wrapper.eq(Constants.OPEN_ID,openId).eq(Constants.STATUS,UserApplyStatusEnum.apply.getCode())
                .orderByDesc(Constants.ID);
        Page<UserApplyDO> userApplyDOPage = userApplyMapper.selectPage(pageof, wrapper);
        if(CollectionUtils.isEmpty(userApplyDOPage.getRecords())){
            return null;
        }
        List<CourseInfoDO> courseInfoDOList = userApplyDOPage.getRecords().stream().map(o -> courseInfoMapper.selectById(o.getCourseId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(courseInfoDOList)){
            log.error("getMyApplyByPage  获取课程信息为空,userApplyDO:{}",userApplyDOPage.getRecords());
            return null;
        }
        List<CourseInfoResponse> responses = courseInfoDOList.stream().map(o -> {
            CourseInfoResponse courseInfoResponse = courseInfoToRes(o);
            courseInfoResponse.setApplied(true);
            courseInfoResponse.setDistance(calDistanceService.calDistanceByOpenId(openId, o.getPoint()));
            return courseInfoResponse;
        }).collect(Collectors.toList());

        return responses;

    }

    @Override
    public List<CourseInfoResponse> getMyFavoriteByPage(String openId, Integer pageSize, Integer page) {
        Page<UserFavoriteDO> pageof = Page.of(page,pageSize);
        QueryWrapper<UserFavoriteDO> wrapper = new QueryWrapper<>();
        wrapper.eq(Constants.OPEN_ID,openId).eq(Constants.STATUS, UserFavoriteStatusEnum.favorite.getCode())
                .orderByDesc(Constants.ID);
        Page<UserFavoriteDO> userFavoriteDOPage = userFavoriteMapper.selectPage(pageof, wrapper);
        if(CollectionUtils.isEmpty(userFavoriteDOPage.getRecords())){
            return null;
        }
        List<CourseInfoDO> courseInfoDOList = userFavoriteDOPage.getRecords().stream().map(o -> courseInfoMapper.selectById(o.getCourseId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(courseInfoDOList)){
            log.error("getMyApplyByPage  获取课程信息为空,userApplyDO:{}",userFavoriteDOPage.getRecords());
            return null;
        }
        List<CourseInfoResponse> responses = courseInfoDOList.stream().map(o -> {
            CourseInfoResponse courseInfoResponse = courseInfoToRes(o);
            courseInfoResponse.setFavorited(true);
            courseInfoResponse.setDistance(calDistanceService.calDistanceByOpenId(openId, o.getPoint()));
            return courseInfoResponse;
        }).collect(Collectors.toList());

        return responses;
    }



}
