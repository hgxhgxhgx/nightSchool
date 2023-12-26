package com.tencent.wxcloudrun.model.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseInfoStatusEnum {

    CHECKING("审核中",1),
    CHECK_FAIL("审核失败",2),
    GROUP_BOOKING("拼团中",3),
    GROUP_BOOK_FAIL("拼团失败",4),
    COURSE_BEGIN("开课中",5),
    FINISH("已结束",6);


    private final String desc;
    private final Integer code;

}
