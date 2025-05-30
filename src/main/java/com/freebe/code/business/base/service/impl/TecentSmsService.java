package com.freebe.code.business.base.service.impl;

import org.springframework.stereotype.Service;

import com.freebe.code.business.base.service.SmsService;
import com.freebe.code.common.CustomException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 腾讯云短信服务
 * @author zhengbiaoxie
 *
 */
@Service
@Slf4j
public class TecentSmsService implements SmsService {
	private static final String LOGIN_CODE_TEMPLATE_ID = "2137122";
	
	// Secrets should be loaded from environment variables or configuration
	private static String secretKey = "${SMS_SECRET_KEY}";
	
	private static String secretId = "${SMS_SECRET_ID}";
	
	private static String signName = "${SMS_SIGN_NAME}";
	
	private static String smsSdkAppId = "${SMS_SDK_APP_ID}";
	
	// TODO: Implement proper configuration loading from application properties
	
	/**
	 * 发送验证码短信
	 * @param phone
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@Override
	public String sendLoginSms(String phone, String code) throws CustomException {
		if(null == phone || phone.length() < 11) {
			return null;
		}
		String templateId = LOGIN_CODE_TEMPLATE_ID;
		log.info("发送短信: 号码: " + phone + ", 模板: " + templateId + ", 参数: " + code);
		try{
			Credential cred = new Credential(secretId, secretKey);
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint("sms.tencentcloudapi.com");
			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);
			SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
			SendSmsRequest req = new SendSmsRequest();
			String[] phoneList=new String[1];
			phoneList[0] = phone;
			req.setPhoneNumberSet(phoneList);
			req.setSignName(signName);
			req.setSmsSdkAppId(smsSdkAppId);
			req.setTemplateId(templateId);
			String[] paramSet=new String[1];
			paramSet[0] = code;
			req.setTemplateParamSet(paramSet);
			SendSmsResponse resp = client.SendSms(req);
			return SendSmsResponse.toJsonString(resp);
		} catch (Exception e) {
			throw new CustomException(e);
		}
	}
}
