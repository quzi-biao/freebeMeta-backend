package com.freebe.code.business.meta.type;

/**
 * 任务状态
 * @author zhengbiaoxie
 *
 */
public class TaskState {
	/**
	 * 等待认领
	 */
	public static final int WAIT_TAKER = 0;
	
	/**
	 * 任务进行中
	 */
	public static final int RUNNING = 1;
	
	/**
	 * 任务完成待审核
	 */
	public static final int WAIT_AUDIT = 2;
	
	/**
	 * 任务完成
	 */
	public static final int DONE = 3;
	
	/**
	 * 任务审核不通过，审核不通过表示任务失败，发布者和认领者都有责任
	 */
	public static final int AUDIT_FAILED = 4;
	
	/**
	 * 超时取消
	 */
	public static final int TIMEOUT_FAILED = 5;
	
	/**
	 * 认领超时
	 */
	public static final int WAIT_TIMEOUT_FAILED = 6;
	
	/**
	 * 直接取消
	 */
	public static final int CANCEL = 7;
}
