package com.freebe.code.business.meta.type;

/**
 * 岗位申请状态
 * @author zhengbiaoxie
 *
 */
public class ApplyStatus {
	/**
	 * 等待答题
	 */
	public static final int ANSWER = 1;
	
	/**
	 * 答题审核
	 */
	public static final int ANSWER_AUDIT = 2;
	
	/**
	 * 答题通过
	 */
	public static final int ANSWER_PASS = 3;
	
	/**
	 * 答题未通过
	 */
	public static final int ANSWER_REJECT = 4;
	
	/**
	 * 任务进行中
	 */
	public static final int DO_TASK = 5;
	
	/**
	 * 任务审核
	 */
	public static final int TASK_AUDIT = 6;
	
	/**
	 * 任务通过
	 */
	public static final int TASK_PASS = 7;
	
	/**
	 * 任务未通过
	 */
	public static final int TASK_REJECT = 8;
	
	/**
	 * 申请审核
	 */
	public static final int REVIEW = 9;
	
	/**
	 * 审核通过
	 */
	public static final int REVIEW_PASS = 10;
	
	/**
	 * 审核未通过
	 */
	public static final int REVIEW_REJECT = 11;
}
