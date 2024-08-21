package com.freebe.code.business.meta.type;

/**
 * 项目结算状态
 * @author zhengbiaoxie
 *
 */
public class ProjectBillState {
	
	/**
	 * 未结算
	 */
	public static final int NONE = 0;
	
	/**
	 * 结算中
	 */
	public static final int BILLING = 1;
	
	/**
	 * 结算失败
	 */
	public static final int FAILED = 2;
	
	/**
	 * 结算完成
	 */
	public static final int DONE = 3;
}
