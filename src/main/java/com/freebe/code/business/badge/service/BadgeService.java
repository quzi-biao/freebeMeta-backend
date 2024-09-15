package com.freebe.code.business.badge.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.badge.controller.param.BadgeParam;
import com.freebe.code.business.badge.controller.param.BadgeQueryParam;
import com.freebe.code.business.badge.entity.Badge;
import com.freebe.code.business.badge.vo.BadgeVO;
import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface BadgeService extends BaseService<Badge> {

	BadgeVO findById(Long id) throws CustomException;

	BadgeVO createOrUpdate(BadgeParam param) throws CustomException;

	Page<BadgeVO> queryPage(BadgeQueryParam param) throws CustomException;
	
	/**
	 * 徽章持有者数量加 1
	 * @param badgeId
	 */
	void incHolderNumber(Long badgeId) throws CustomException;
	
	/**
	 * 徽章持有者数量减 1
	 * @param badgeId
	 * @throws CustomException 
	 */
	void decHolderNumber(Long badgeId) throws CustomException;


}
