package com.freebe.code.business.base.service.impl;

import lombok.Data;

@Data
public class UserSmsCodeInfo {
    private String code;
    private Long createTime;
    private Integer tryTime;
}
