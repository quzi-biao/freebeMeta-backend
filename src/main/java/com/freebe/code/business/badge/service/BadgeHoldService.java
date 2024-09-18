package com.freebe.code.business.badge.service;

import java.util.List;

import com.freebe.code.business.badge.controller.param.BadgeActionParam;
import com.freebe.code.business.badge.controller.param.BadgeHoldParam;
import com.freebe.code.business.badge.controller.param.BadgeHoldQueryParam;
import com.freebe.code.business.badge.entity.BadgeHold;
import com.freebe.code.business.badge.vo.BadgeHoldVO;
import com.freebe.code.business.badge.vo.BadgeVO;
import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface BadgeHoldService extends BaseService<BadgeHold> {
	// 徽章管理员
	public static final String BADGE_MANAGER = "BM";

	BadgeHoldVO findById(Long id) throws CustomException;

	BadgeHoldVO createOrUpdate(BadgeHoldParam param) throws CustomException;

	List<BadgeHoldVO> queryHold(BadgeHoldQueryParam param) throws CustomException;
	
	/**
	 * 授予徽章
	 * @param param
	 * @return
	 */
	BadgeHoldVO awardBadge(BadgeActionParam param) throws CustomException;
	
	/**
	 * 收回徽章
	 * @param param
	 * @return
	 */
	BadgeHoldVO takeBackBadge(BadgeActionParam param) throws CustomException;
	
	/**
	 * 放弃徽章
	 * @param param
	 * @return
	 */
	BadgeHoldVO giveOutBadge(BadgeActionParam param) throws CustomException;

	/**
	 * 查询某个成员持有的徽章列表
	 * @param id
	 * @return
	 * @throws CustomException 
	 */
	List<BadgeVO> queryMemberBadges(Long id) throws CustomException;

}
