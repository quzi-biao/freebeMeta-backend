package com.freebe.code.business.meta.type;

/**
 * 交易状态
 * @author zhengbiaoxie
 *
 */
public class TransactionState {
	/**
	 * 待审核
	 */
	public static final int WAIT_AUDIT = 0;
	
	/**
	 * 公示中
	 */
	public static final int PUBLICITY = 1;
	
	/**
	 * 上链交易中
	 */
	public static final int CONFIRMING = 2;
	
	/**
	 * 交易已确认
	 */
	public static final int CONFIRM = 3;
	
	/**
	 * 交易失败
	 */
	public static final int FAILED = 4;
	
	/**
	 * 交易取消
	 */
	public static final int CANCEL = 5;
}
