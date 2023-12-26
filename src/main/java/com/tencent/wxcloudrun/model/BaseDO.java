package com.tencent.wxcloudrun.model;

import annotation.CreatedAt;
import annotation.Id;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tencent.wxcloudrun.model.enumList.GenerationType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

@Getter
@Setter
@FieldNameConstants
public class BaseDO {
    @Id(strategy = GenerationType.AUTO)
    Long id;

    @TableField(fill = FieldFill.INSERT)
    Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date gmtModify;

}
