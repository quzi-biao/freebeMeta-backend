package com.freebe.code.business.meta.type;

/**
 * 悬赏创建约束
 * @author zhengbiaoxie
 *
 */
public class BountyCreateLimit {
	//悬赏创建约束，1 所有人可创建，2 项目成员可创建，3 主理人可以创建
	
	public static final int ALL = 1;
	
	public static final int MEMBER = 2;
	
	public static final int OWNER = 3;
}
