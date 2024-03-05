package com.tencent.wxcloudrun.model.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserFavoriteStatusEnum {

    favorite("已收藏",1),
    cancelFavorite("取消收藏",2);


    private final String desc;
    private final Integer code;

}
