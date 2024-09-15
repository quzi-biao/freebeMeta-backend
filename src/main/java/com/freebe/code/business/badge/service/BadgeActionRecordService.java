package com.freebe.code.business.badge.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.badge.entity.BadgeActionRecord;
import com.freebe.code.business.badge.vo.BadgeActionRecordVO;
import com.freebe.code.business.badge.controller.param.BadgeActionParam;
import com.freebe.code.business.badge.controller.param.BadgeActionRecordQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface BadgeActionRecordService extends BaseService<BadgeActionRecord> {

	BadgeActionRecordVO findById(Long id) throws CustomException;

	BadgeActionRecordVO createOrUpdate(BadgeActionParam param) throws CustomException;

	Page<BadgeActionRecordVO> queryPage(BadgeActionRecordQueryParam param) throws CustomException;

}
