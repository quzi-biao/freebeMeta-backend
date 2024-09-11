package com.freebe.code.business.advanture.type;

public class AdvantureTaskTakeState {
	
	/**
	 * 进行中
	 */
	public static final int DOING = 0;
	
	/**
	 * 等待审核
	 */
	public static final int WAIT_AUDIT = 1;
	
	/**
	 * 已完成
	 */
	public static final int DONE = 2;
	
	/**
	 * 审核未通过
	 */
	public static final int FAILED = 3;
	
	/**
	 * 放弃任务
	 */
	public static final int GIVEOUT = 4;
}
