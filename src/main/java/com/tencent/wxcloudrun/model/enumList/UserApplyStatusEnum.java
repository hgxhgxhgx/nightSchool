package com.tencent.wxcloudrun.model.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserApplyStatusEnum {

    apply("已报名",1),
    cancelApply("取消报名",2);


    private final String desc;
    private final Integer code;

}
