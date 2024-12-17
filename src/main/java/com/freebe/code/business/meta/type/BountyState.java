package com.freebe.code.business.meta.type;

/**
 * 悬赏状态
 * @author zhengbiaoxie
 *
 */
public class BountyState {
	/**
	 * 等待认领
	 */
	public static final int WAIT_TAKER = 0;
	
	/**
	 * 悬赏进行中
	 */
	public static final int RUNNING = 1;
	
	/**
	 * 悬赏完成待审核
	 */
	public static final int WAIT_AUDIT = 2;
	
	/**
	 * 悬赏完成
	 */
	public static final int DONE = 3;
	
	/**
	 * 悬赏审核不通过，审核不通过表示悬赏失败，发布者和认领者都有责任
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
	
	/**
	 * 等待前置任务完成
	 */
	public static final int WAIT_DEPEND = 8;
}
