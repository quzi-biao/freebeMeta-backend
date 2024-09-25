package com.freebe.code.business.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.freebe.code.business.base.controller.param.LoginParam;
import com.freebe.code.business.base.controller.param.UpdatePasswordParam;
import com.freebe.code.business.base.entity.User;
import com.freebe.code.business.base.repository.UserRepository;
import com.freebe.code.business.base.service.SmsService;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.business.meta.controller.param.WalletParam;
import com.freebe.code.business.meta.service.WalletService;
import com.freebe.code.business.meta.type.Currency;
import com.freebe.code.common.CharChecker;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.KVStorage;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.CodeUtils;
import com.freebe.code.util.JwtUtils;
import com.freebe.code.util.RandomNickName;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{
	
	private static final String FREEBE_ID_SUFFIX = ".freebe";

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private KVStorage kv;

	@Autowired
	private VerifyCodeManager codeManager;
	
	private final Integer smsSize=6;

	
	@PostConstruct
	public void init() throws CustomException {
		if(this.repository.count() == 0) {
			// 创建默认账户
			User user = new User();
			user.setName("FreeBe");
			user.setIsDelete(false);
			user.setFreeBeId("01.freebe");
			user.setPassword(JwtUtils.getHashSecret("123456"));
			user.setCreateTime(System.currentTimeMillis());
			user.setCode(CodeUtils.generateCode(User.class));
			user.setInUse(true);
			user = this.repository.save(user);
			user.setContribution(0L);
			
			WalletParam param = new WalletParam();
			param.setUserId(user.getId());
			walletService.createOrUpdate(param);
		}
	}
	
	@Override
	public UserVO getUser(Long userId) {
		if(userId == null || userId < 0) {
			return null;
		}
		try {
			User u = this.objectCaches.get(userId, User.class);
			if(null != u) {
				return toVO(u);
			}
			
			User user = this.getReference(userId);
			objectCaches.put(user.getId(), user);
	
			return toVO(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void addContribution(Long userId, Long added) throws CustomException {
		User user = this.getReference(userId);
		if(null == user) {
			return;
		}
		
		Long c = user.getContribution();
		if(null == c) {
			user.setContribution(0L);
			c = 0L;
		}
		user.setContribution(c + added);
		this.repository.save(user);
	}
	

	@Override
	public UserVO getOrCreateUserByAddress(String address) throws CustomException {
		User e = new User();
		e.setAddress(address);
		e.setIsDelete(false);
		
		User user = this.find(Example.of(e));
		if(null == user) {
			user = new User();
			user.setName("F." + RandomNickName.create());
			user.setIsDelete(false);
			user.setCreateTime(System.currentTimeMillis());
			user.setCode(CodeUtils.generateCode(User.class));
			user.setInUse(true);
			user.setVerifyInfo(toUniqueId(address));
			user.setFreeBeId("");
			user.setAddress(address);
			user.setContribution(0L);
			user = this.repository.save(user);
			
			WalletParam param = new WalletParam();
			param.setUserId(user.getId());
			walletService.createOrUpdate(param);
		}
		
		user.setLastLogin(System.currentTimeMillis());
		this.repository.save(user);
		
        return toVO(user);
	}
	
	@Override
	public UserVO updateUserInfo(String name, String avator, String freebeId) throws CustomException {
		if(StringUtils.isEmpty(freebeId)) {
			throw new CustomException("请设置FreeBeId");
		}
		if(!CharChecker.checkFreeBeId(freebeId)) {
			throw new CustomException("FreeBe ID只能由数字和字母组成（区分大小写）");
		}
		
		if(!freebeId.endsWith(".freebe")) {
			throw new CustomException("FreeBe ID不合法");
		}
		
		UserVO user = getCurrentUser();
		User u = this.getReference(user.getId());
		if(null == u) {
			throw new CustomException("用户不存在");
		}
		
		u.setName(name);
		if(null != avator) {
			u.setAvator(avator);
		}
		if(null != freebeId) {
			u.setFreeBeId(freebeId);
		}
		
		u = this.repository.save(u);
		
		user = toVO(u);
		
		objectCaches.put(user.getId(), u); 
		
		return user;
	}

	@Override
	public void deleteAuthentication(String token) {
		//后面需要更新kv的删除功能
		this.kv.save(token, System.currentTimeMillis() - 40*60 * 1000);
	}

	@Override
	public void validateAuthentication(String token) throws CustomException {
		String ts = this.kv.get(token);
		// TODO 时间戳过期判断
		if(null == ts) {
			throw new CustomException("token 不存在");
		}
	}

	@Override
	public String login(LoginParam loginParam) throws CustomException {
		User user = this.getOrCreateUser(loginParam);
		if(null == user) {
			throw new CustomException("用户不存在");
		}
		
		String uniqueCode = user.getVerifyInfo();
        
        String password = loginParam.getPassword();
        password = JwtUtils.getHashSecret(password);
		//增加空密码校验
		if(null == user.getPassword()){
			throw new CustomException("用户名或密码错误");
		}
        if (!user.getPassword().equals(password)) {
        	codeManager.updateAttemptedRecord(uniqueCode);
            throw new CustomException("用户名或密码错误");
        }
		else {
			//更新尝试次数的map
			codeManager.updateUniqueCode(uniqueCode);
		}
		
		user.setLastLogin(System.currentTimeMillis());
		this.repository.save(user);
        
        // 设置token
        String token = JwtUtils.getToken(user.getId().toString(), user.getPassword());
        this.kv.save(token, System.currentTimeMillis());
        return token;
	}

	@Override
	public Long updatePassword(@Valid UpdatePasswordParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getNewPassword())) {
			throw new CustomException("参数错误");
		}
		
		User user = this.getById(getCurrentUser().getId());
		
		if(null == user) {
			throw new CustomException("用户不存在");
		}
		
		String oldPassword = param.getOldPassword();
		if(!StringUtils.isEmpty(oldPassword)) {
			oldPassword = JwtUtils.getHashSecret(oldPassword);
			if(null == oldPassword || !oldPassword.equals(user.getPassword())) {
				throw new CustomException("旧密码输入错误");
			}
		}
		
		user.setPassword(JwtUtils.getHashSecret(param.getNewPassword()));
		
		this.update(user.getId(), user);
		
		return user.getId();
	}
	
	@Override
	public String register(LoginParam loginParam) throws CustomException {
		String uniqueId = checkParamAndGetUniqueId(loginParam);
		
		User user = this.findUserByUniqueId(uniqueId);
		if(null != user) {
			throw new CustomException("邮箱已注册: " + user.getFreeBeId());
		}
		user = this.findUserByFreeBeId(loginParam.getFreeBeId());
		if(null != user) {
			throw new CustomException("FreeBeId 已使用, 请换一个");
		}
		
		// 如果用户不存在，则创建
		user = new User();
		user.setName(loginParam.getFreeBeId().split("\\.")[0]);
		if(user.getName() == null || user.getName().length() == 0) {
			user.setName("F." + RandomNickName.create());
		}
		user.setIsDelete(false);
		user.setCreateTime(System.currentTimeMillis());
		user.setCode(CodeUtils.generateCode(User.class));
		user.setInUse(true);
		user.setVerifyInfo(uniqueId);
		user.setFreeBeId(loginParam.getFreeBeId());
		user.setPassword(JwtUtils.getHashSecret(loginParam.getPassword()));
		user.setContribution(0L);
		user = this.repository.save(user);
		
		WalletParam param = new WalletParam();
		param.setUserId(user.getId());
		walletService.createOrUpdate(param);
		
		String token = JwtUtils.getToken(user.getId().toString(), user.getPassword());
        this.kv.save(token, System.currentTimeMillis());
        return token;
	}


	@Override
	public String getSmsByPersonId(String personId) throws CustomException{
		if(StringUtils.isEmpty(personId)) {
			return "请输入邮箱地址";
		}
		String uniqueId = toUniqueId(personId);
		//1.创建验证码
		String code = RandomUtil.randomNumbers(smsSize);
		//每一个号码半个小时只能发送3次，逻辑参考登录验证
		if(codeManager.checkCodeAttemptTime(uniqueId)){
			throw new CustomException("半个小时内只能发送三次验证码，请稍后重试");
		}
		
		if(personId.indexOf("@") < 0) {
			//2.发送验证码
			smsService.sendLoginSms(personId, code);
		}else {
			emailService.sendVerifyCode(personId, code);
		}
		
		codeManager.updateCodeAttemptedRecord(uniqueId,code);
		return "短信验证码发送成功";
	}
	

	@Override
	public List<Long> queryUserByName(String takerName) {
		User user = new User();
		user.setName(takerName);
		user.setIsDelete(false);
		List<User> users = this.repository.findAll(Example.of(user));
		if(null == users || users.size() == 0) {
			return null;
		}
		
		List<Long> ret = new ArrayList<>();
		for(User u : users) {
			ret.add(u.getId());
		}
		
		return ret;
	}

	
	private User getOrCreateUser(LoginParam loginParam) throws CustomException {
		 // 校验用户密码
		User user = null;
		if(StringUtils.isEmpty(loginParam.getAccountId())) {
			if(StringUtils.isEmpty(loginParam.getFreeBeId())) {
				throw new CustomException("登录参数错误");
			}else {
				user = this.findUserByFreeBeId(loginParam.getFreeBeId());
			}
		}else {
			if(StringUtils.isEmpty(loginParam.getAccountId())) {
				throw new CustomException("登录参数错误");
			}
			user = this.findUserByUniqueId(toUniqueId(loginParam.getAccountId()));
		}
		
		return user;
	}
	
	/**
	 * 根据手机号码查找用户
	 * @param mobile
	 * @return
	 */
	public User findUserByUniqueId(String uniqueId) {
		User user = new User();
		user.setVerifyInfo(uniqueId);
		user.setIsDelete(false);
		
		return this.find(Example.of(user));
	}
	
	/**
	 * 根据 FreeBe 查找用户
	 * @param freeBeId
	 * @return
	 */
	public User findUserByFreeBeId(String freeBeId) {
		User user = new User();
		user.setFreeBeId(freeBeId);
		user.setIsDelete(false);
		
		return this.find(Example.of(user));
	}
	
	private static String toUniqueId(String personId) {
		byte[] data = personId.getBytes();
		SHA256Digest d = new SHA256Digest();
		d.update(data, 0, data.length);
		byte[] ret = new byte[256];
		d.doFinal(ret, 0);
		
		String md5 = MD5.create().digestHex(personId);
		String sha = HexUtils.toHexString(data);
		
		return MD5.create().digestHex(md5 + sha);
	}
	
	private UserVO toVO(User user) throws CustomException {
		if(null == user) {
			return null;
		}
		UserVO vo = new UserVO();
		vo.setId(user.getId());
		vo.setName(user.getName());
		vo.setAddress(user.getAddress());
		vo.setAvator(user.getAvator());
		vo.setFreeBeId(user.getFreeBeId());
		vo.setLastLogin(user.getLastLogin());
		vo.setContribution(user.getContribution());
		vo.setFreeBe(this.walletService.getAmount(user.getId(), Currency.FREE_BE));
		
		return vo;
	}
	
	/**
	 * 检查登录参数，并返回唯一ID
	 * @param loginParam
	 * @return
	 * @throws CustomException
	 */
	private String checkParamAndGetUniqueId(LoginParam loginParam) throws CustomException {
		if(StringUtils.isEmpty(loginParam.getAccountId())) {
			throw new CustomException("请输入邮箱地址");
		}
		if(!CharChecker.checkEmail(loginParam.getAccountId())) {
			throw new CustomException("您输入不是邮箱地址");
		}
		if(StringUtils.isEmpty(loginParam.getFreeBeId())) {
			throw new CustomException("请设置FreeBeId");
		}
		if(!CharChecker.checkFreeBeId(loginParam.getFreeBeId())) {
			throw new CustomException("FreeBe ID只能由数字和字母组成（区分大小写）");
		}
		
		String freeBeName = loginParam.getFreeBeId().split("\\.")[0];
		if(StringUtils.isEmpty(freeBeName)) {
			throw new CustomException("请设置合法FreeBeId");
		}
		
		if(StringUtils.isEmpty(loginParam.getPassword())) {
			throw new CustomException("请设置密码");
		}
		if(!CharChecker.checkPassword(loginParam.getPassword())) {
			throw new CustomException("密码至少为6位的两种字符组合");
		}
		loginParam.setFreeBeId(loginParam.getFreeBeId() + FREEBE_ID_SUFFIX);
		
		String uniqueId = toUniqueId(loginParam.getAccountId());
		UserSmsCodeInfo userSmsCodeInfo = this.codeManager.codeRecord.getOrDefault(uniqueId ,null);
		if(null == userSmsCodeInfo){
			throw new CustomException("验证码错误");
		}
		
		String smsCode = loginParam.getSmsCode();

		if (!userSmsCodeInfo.getCode().equals(smsCode) || System.currentTimeMillis()-userSmsCodeInfo.getCreateTime()>10*3600*1000) {
			codeManager.updateAttemptedRecord(uniqueId);
			throw new CustomException("验证码错误");
		}
		return uniqueId;
	}

}
