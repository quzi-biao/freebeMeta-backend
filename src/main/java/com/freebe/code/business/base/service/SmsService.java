package com.freebe.code.business.base.service;

import com.freebe.code.common.CustomException;

/**
 * 短信服务
 * @author zhengbiaoxie
 *
 */
public interface SmsService {

	String sendLoginSms(String phone, String code) throws CustomException;
}
