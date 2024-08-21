package com.freebe.code.business.meta.type;

/**
 * 项目状态
 * @author zhengbiaoxie
 *
 */
public class ProjectState {
	
	/**
	 * 创建中
	 */
	public static final int CREATING = 1;
	
	/**
	 * 成员招募中
	 */
	public static final int INVITING = 2;
	
	/**
	 * 成员招募结束，等待开始
	 */
	public static final int WAIT_START = 3;
	
	/**
	 * 项目进行中
	 */
	public static final int RUNNING = 4;
	
	/**
	 * 项目完成
	 */
	public static final int DONE = 5;
	
	/**
	 * 项目取消
	 */
	public static final int CANCEL = 6;
	
	/**
	 * 项目暂停
	 */
	public static final int STOP = 7;
	
	/**
	 * 项目结算超时
	 */
	public static final int BILLING_TIMEOUT = 8;
	
	/**
	 * 结算失败
	 */
	public static final int BILLING_FAILED = 10;
	
	/**
	 * 项目延期
	 */
	public static final int DELAY = 9;
	
	/**
	 * 项目正常结束
	 */
	public static final int NORMAL_END = 99;
}
