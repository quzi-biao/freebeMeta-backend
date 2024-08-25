package com.freebe.code.business.meta.type;

/**
 * 任务认领状态
 * @author zhengbiaoxie
 *
 */
public class TaskTakerState {
	/**
	 * 状态正常
	 */
	public static final int NORMAL = 0;
	
	/**
	 * 已完成
	 */
	public static final int DONE = 1;
	
	/**
	 * 超时取消
	 */
	public static final int TIMEOUT_CANCEL = 2;
	
	/**
	 * 超时取消
	 */
	public static final int AUDIT_FAILED = 3;
	
	/**
	 * 放弃项目
	 */
	public static final int GIVE_OUT = 4;
}
