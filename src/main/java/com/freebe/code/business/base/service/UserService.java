package com.freebe.code.business.base.service;

import com.freebe.code.business.base.controller.param.LoginParam;
import com.freebe.code.business.base.controller.param.UpdatePasswordParam;
import com.freebe.code.business.base.entity.User;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.common.CustomException;

public interface UserService extends BaseService<User> {
	
	/**
	 * 用户登录
	 * @param loginParam
	 * @return
	 * @throws CustomException 
	 */
	String login(LoginParam loginParam) throws CustomException;
	
	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 */
	UserVO getUser(Long userId);
	
	/**
	 * 根据地址查找用户
	 * @param address
	 * @return
	 * @throws CustomException 
	 */
	UserVO getOrCreateUserByAddress(String address) throws CustomException;
	
	
	/**
	 * 更新用户密码
	 * @param param
	 * @return
	 * @throws CustomException 
	 */
	Long updatePassword(UpdatePasswordParam param) throws CustomException;
	
	/**
	 * 更新信息
	 * @param name
	 * @param avator
	 * @param freebeId 
	 * @return
	 * @throws CustomException
	 */
	UserVO updateUserInfo(String name, String avator, String freebeId) throws CustomException;

	/**
	 * 根据个人的手机号/邮箱等获取验证码
	 * @param phone
	 * @return
	 * @throws CustomException
	 */
	String getSmsByPersonId(String account) throws CustomException;

	/**
	 * 
	 * @param token
	 */
	void deleteAuthentication(String token);

	/**
	 *
	 * @param token
	 */
	void validateAuthentication(String token) throws CustomException;

	/**
	 * 用户注册
	 * @param loginParam
	 * @return
	 * @throws CustomException
	 */
	String register(LoginParam loginParam) throws CustomException;

}
