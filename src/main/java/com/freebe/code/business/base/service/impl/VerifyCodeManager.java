package com.freebe.code.business.base.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class VerifyCodeManager {

	private Map<String,String> attemptRecord = new HashMap<>();;

	public Map<String, UserSmsCodeInfo> codeRecord = new HashMap<>();
	
	/**
	 * 频率控制
	 * @param uniqueId
	 * @return
	 */
	public Boolean checkAttemptTime(String uniqueId){
		String timeAndRecord = attemptRecord.getOrDefault(uniqueId,"0");
		if(timeAndRecord.equals("0")){
			return false;
		}
		String[] parts = timeAndRecord.split("_");
		String recordTime = parts[0];
		String attemptTime = parts[1];
		//第一次尝试的时间超过10分钟
		if(System.currentTimeMillis()-Long.valueOf(recordTime)>10*60*1000){
			attemptRecord.remove(uniqueId);
			return false;
		}
		else {
            return Integer.parseInt(attemptTime) >= 5;
		}
    }
	
	/**
	 * 检查该账号 ID 是否重复输入
	 * @param personId 用户邮箱或手机号，需进行 Hash 计算
	 * @return
	 */
	public Boolean checkCodeAttemptTime(String uniqueId){
		UserSmsCodeInfo timeAndRecord = codeRecord.getOrDefault(uniqueId,null);
		if(null == timeAndRecord){
			return false;
		}
		Long recordTime = timeAndRecord.getCreateTime();
		Integer attemptTime = timeAndRecord.getTryTime();
		//第一次尝试的时间超过30分钟
		if(System.currentTimeMillis()-recordTime>30*60*1000){
			codeRecord.remove(uniqueId);
			return false;
		}
		else {
			return attemptTime >= 3;
		}
	}
	
	/**
	 * 设置 ID 对应的 code
	 * @param uniqueId
	 * @param code
	 */
	public void updateCodeAttemptedRecord(String uniqueId,String code){
		UserSmsCodeInfo timeAndRecord = codeRecord.getOrDefault(uniqueId,null);
		if(null == timeAndRecord){
			UserSmsCodeInfo smsCodeInfo =new UserSmsCodeInfo();
			smsCodeInfo.setCreateTime(System.currentTimeMillis());
			smsCodeInfo.setCode(code);
			smsCodeInfo.setTryTime(1);
			codeRecord.put(uniqueId,smsCodeInfo);
		}
		else {
			timeAndRecord.setTryTime(timeAndRecord.getTryTime()+1);
			timeAndRecord.setCode(code);
		}
	}

	/**
	 * 更新尝试登录的次数
	 * @param uniqueId 通过用户个人账号（邮箱/手机号）加密生成的唯一 ID
	 */
	public void updateAttemptedRecord(String uniqueId){
		String timeAndRecord = attemptRecord.getOrDefault(uniqueId,"0");
		if(timeAndRecord.equals("0")){
			String attepTime=String.valueOf(System.currentTimeMillis())+"_"+"1";
			attemptRecord.put(uniqueId,attepTime);
		}
		else {
			String[] parts = timeAndRecord.split("_");
			String recordTime = parts[0];
			String attemptTime = parts[1];
			String attepTime=recordTime+"_"+(Integer.parseInt(attemptTime)+1);
			attemptRecord.put(uniqueId,attepTime);
		}
	}

	/**
	 * 更新 ID
	 * @param uniqueCode 通过用户个人账号（邮箱/手机号）加密生成的唯一 ID
	 */
	public void updateUniqueCode(String uniqueId) {
		attemptRecord.put(uniqueId, "0");
	}
}
