package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.ContentDraft;
import com.freebe.code.business.meta.vo.ContentDraftVO;
import com.freebe.code.business.meta.controller.param.ContentDraftParam;
import com.freebe.code.business.meta.controller.param.ContentDraftQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface ContentDraftService extends BaseService<ContentDraft> {

	ContentDraftVO findById(Long id) throws CustomException;

	ContentDraftVO createOrUpdate(ContentDraftParam param) throws CustomException;

	Page<ContentDraftVO> queryPage(ContentDraftQueryParam param) throws CustomException;
	
	/**
	 * 内容已发布
	 * @param contentId
	 * @throws CustomException
	 */
	void deploy(Long draftId, Long contentId) throws CustomException;

}
