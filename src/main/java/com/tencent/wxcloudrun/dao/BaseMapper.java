package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T>  {

    //插入
    int insert(T entity);


    //删除
    int deleteById(Serializable id);

    int deleteById(T entity);

    /**
     * 根据表字段删除数据，columnMap为字段->值的映射。
     * @param columnMap
     * @return
     */
    int deleteByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    int deleteBatchIds(@Param(Constants.COLLECTION) Collection<?> idList);


    //更新
    int updateById(@Param(Constants.ENTITY) T entity);

    int update(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);

    //哈讯
    T selectById(Serializable id);

    List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);
}
