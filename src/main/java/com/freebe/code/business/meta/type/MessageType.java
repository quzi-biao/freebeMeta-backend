package com.freebe.code.business.meta.type;

/**
 * 消息类型
 * @author zhengbiaoxie
 *
 */
public class MessageType {
	
	/**
	 * 项目邀请
	 */
	public static final int PROJECT_INVITE = 1;
	
	/**
	 * 系统消息
	 */
	public static final int SYSTEM = 2;
	
	/**
	 * 用户发送的消息
	 */
	public static final int USER_MESSAGE = 3;
	
	/**
	 * 内容评论消息
	 */
	public static final int CONTENT_COMMENT_MSG = 10;
	
	/**
	 * 内容喜欢
	 */
	public static final int CONTENT_LIKE_MSG = 11;
	
	/**
	 * 内容被收藏的消息
	 */
	public static final int CONTENT_COLLECT_MSG = 12;
	
	/**
	 * 内容审核消息
	 */
	public static final int CONTENT_AUDIT = 13;
	
	
	/**
	 * 悬赏被领取的消息
	 */
	public static final int BOUNTY_TAKE_MSG = 21;
	
	/**
	 * 悬赏提交的消息
	 */
	public static final int BOUNTY_SUBMIT_MSG = 22;
	
	/**
	 * 悬赏修改提交的消息
	 */
	public static final int BOUNTY_SUBMIT_CHANGE_MSG = 23;
	
	/**
	 * 悬赏评审的消息
	 */
	public static final int BOUNTY_AUDIT_MSG = 24;
	
	/**
	 * 悬赏审核结果的消息
	 */
	public static final int BOUNTY_AUDIT_RESULT_MSG = 25;
	
	/**
	 * 交易创建的消息
	 */
	public static final int TRANSACTION_CREATE_MSG = 31;
	
	/**
	 * 交易到账的消息
	 */
	public static final int TRANSACTION_END_MSG = 32;
	
	/**
	 * 交易失败的消息
	 */
	public static final int TRANSACTION_FAILED_MSG = 33;
	
	/**
	 * 工作申请消息
	 */
	public static final int JOB_APPLY_MSG = 41;
	
	/**
	 * 工作申请问卷回答完成待审核的消息
	 */
	public static final int JO_APPLY_ANSWER_DONE_MSG = 42;
	
	/**
	 * 工作申请问卷审核的消息
	 */
	public static final int JO_APPLY_ANSWER_AUDIT_MSG = 43;
	
	/**
	 * 工作申请任务完成待审核的消息
	 */
	public static final int JO_APPLY_TASK_DONE_MSG = 44;
	
	/**
	 * 工作申请任务审核的消息
	 */
	public static final int JO_APPLY_TASK_AUDIT_MSG = 45;
	
	/**
	 * 工作申请审核的消息
	 */
	public static final int JO_APPLY_REVIEW_MSG = 46;
	
	/**
	 * 内容评论消息
	 */
	public static final int PROVIDE_COMMENT_MSG = 50;
	
	/**
	 * 内容喜欢
	 */
	public static final int PROVIDE_LIKE_MSG = 51;
	
	/**
	 * 内容被收藏的消息
	 */
	public static final int PROVIDE_COLLECT_MSG = 52;
}
