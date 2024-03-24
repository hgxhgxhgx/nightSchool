package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.config.Constants;
import com.tencent.wxcloudrun.dao.mapper.CourseInfoMapper;
import com.tencent.wxcloudrun.dao.mapper.UserApplyMapper;
import com.tencent.wxcloudrun.dao.mapper.UserFavoriteMapper;
import com.tencent.wxcloudrun.dao.mapper.UserInfoMapper;
import com.tencent.wxcloudrun.dto.CourseInfoResponse;
import com.tencent.wxcloudrun.dto.SearchCourseRequest;
import com.tencent.wxcloudrun.model.bizDO.CourseInfoDO;
import com.tencent.wxcloudrun.model.bizDO.UserApplyDO;
import com.tencent.wxcloudrun.model.bizDO.UserFavoriteDO;
import com.tencent.wxcloudrun.model.bizDO.UserInfoDO;
import com.tencent.wxcloudrun.model.enumList.CourseInfoStatusEnum;
import com.tencent.wxcloudrun.model.enumList.UserApplyStatusEnum;
import com.tencent.wxcloudrun.model.enumList.UserFavoriteStatusEnum;
import com.tencent.wxcloudrun.service.CalDistanceService;
import com.tencent.wxcloudrun.service.CourseInfoService;
import com.tencent.wxcloudrun.utils.UserCourseRelationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
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
    public List<CourseInfoResponse> searchCourseByCondition(String openId,
                                                            SearchCourseRequest request) {
        QueryWrapper<CourseInfoDO> wrapper = new QueryWrapper<>();
        wrapper.eq(Constants.STATUS, CourseInfoStatusEnum.GROUP_BOOKING.getCode());
        //过滤时间
        if(request.getDateStart() != null){
            wrapper.ge(Constants.START_TIME, request.getDateStart());
        }
        if(request.getDateEnd() != null){
            wrapper.le(Constants.END_TIME, request.getDateEnd());
        }
        List<CourseInfoDO> courseInfoDOS = courseInfoMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(courseInfoDOS)) {
            log.error("searchCourseByCondition 当前没有符合筛选条件的课程");
            return null;
        }
        if(request.getPage() == null){
            request.setPage(1);
        }
        if(request.getPageSize() == null){
            request.setPageSize(10);
        }
        //过滤距离和keyword  分页
        List<CourseInfoResponse> res =
                courseInfoDOS.stream().filter(e -> hasKeyWord(e.getCourseName(), request.getKeyword())).map(e -> {
                    String distance;
                    if (StringUtils.isNotBlank(request.getUserPoint())) {
                        distance = calDistanceService.calDistanceByPoint(request.getUserPoint(),
                                e.getPoint());
                    } else {
                        distance = calDistanceService.calDistanceByOpenId(openId, e.getPoint());
                    }
                    if (StringUtils.isBlank(distance)) {
                        distance = Constants.ERROR_DISTANCE;
                    }
                    if (request.getDistance() != null && Double.parseDouble(distance) > request.getDistance() * 1000) {
                        return null;
                    }
                    CourseInfoResponse courseInfoResponse = courseInfoToRes(e);
                    courseInfoResponse.setDistance(distance);
                    return courseInfoResponse;
                }).filter(Objects::nonNull).sorted((o1, o2) -> (int) (Double.parseDouble(o1.getDistance()) - Double.parseDouble(o2.getDistance()))).
                        skip((long) (request.getPage() - 1) * request.getPageSize()).limit(request.getPageSize()).collect(Collectors.toList());

        return res;
    }

    private Boolean hasKeyWord(String courseName,String keyWorkds){
        //todo 先是简单的包含逻辑，以后再加上复杂的筛选逻辑
        if(StringUtils.isBlank(courseName)){
            return false;
        }
        if(StringUtils.isBlank(keyWorkds)){
            return true;
        }
        return courseName.contains(keyWorkds);
    }

    @Override
    public List<CourseInfoResponse> getRecommendCourse(String openId, String userPoint, Integer pageSize, Integer page) {
        QueryWrapper<CourseInfoDO> wrapper = new QueryWrapper<>();
        wrapper.eq(Constants.STATUS, CourseInfoStatusEnum.GROUP_BOOKING.getCode());
        List<CourseInfoDO> courseInfoDOS = courseInfoMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(courseInfoDOS)){
            log.error("当前没有正在组团中的课程");
            return null;
        }
        //算距离
        if(StringUtils.isBlank(userPoint)){
            UserInfoDO userInfoDO = userInfoMapper.queryUserInfoByOpenId(openId);
            if(userInfoDO != null && StringUtils.isNotBlank(userInfoDO.getPoint())){
                userPoint = userInfoDO.getPoint();
            }
        }
        String finalUserPoint = userPoint;
        List<CourseInfoResponse> resTotal = courseInfoDOS.stream().map(o -> {
            CourseInfoResponse courseInfoResponse = courseInfoToRes(o);
            if (StringUtils.isNotBlank(finalUserPoint)) {
                courseInfoResponse.setDistance(calDistanceService.calDistanceByPoint(finalUserPoint,
                        courseInfoResponse.getPoint()));
            }else {
                courseInfoResponse.setDistance(Constants.ERROR_DISTANCE);
            }
            return courseInfoResponse;
        }).collect(Collectors.toList());
        //排序
        if(StringUtils.isNotBlank(finalUserPoint)){
            resTotal.sort((o1, o2) -> (int) (Double.parseDouble(o1.getDistance())-Double.parseDouble(o2.getDistance())));
        }
        //分页
        List<CourseInfoResponse> res =
                resTotal.stream().skip((long) (page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());

        return res;
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
