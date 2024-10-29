package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.LikeParam;
import com.freebe.code.business.meta.controller.param.LikeQueryParam;
import com.freebe.code.business.meta.entity.Like;
import com.freebe.code.business.meta.vo.LikeVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface LikeService extends BaseService<Like> {

	LikeVO findById(Long id) throws CustomException;

	LikeVO createOrUpdate(LikeParam param) throws CustomException;

	Page<LikeVO> queryPage(LikeQueryParam param) throws CustomException;

	LikeVO cancel(LikeParam param) throws CustomException;

	boolean isLike(long typeId, Long entityId);

}
