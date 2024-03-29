package com.tencent.wxcloudrun.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {

        this.setFieldValByName("gmtModify", new Date(), metaObject);
        this.setFieldValByName("gmtCreate",new Date(), metaObject);



    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.setFieldValByName("gmtModify", new Date(), metaObject);
    }
}