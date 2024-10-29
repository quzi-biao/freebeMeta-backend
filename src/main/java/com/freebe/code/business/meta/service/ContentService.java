package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.AuditParam;
import com.freebe.code.business.meta.controller.param.ContentParam;
import com.freebe.code.business.meta.controller.param.ContentQueryParam;
import com.freebe.code.business.meta.entity.Content;
import com.freebe.code.business.meta.vo.ContentVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface ContentService extends BaseService<Content> {

	ContentVO findById(Long id) throws CustomException;

	ContentVO createOrUpdate(ContentParam param) throws CustomException;

	Page<ContentVO> queryPage(ContentQueryParam param) throws CustomException;
	
	/**
	 * 从草稿箱发布文章
	 * @return
	 * @throws CustomException
	 */
	ContentVO deployFromDraft(Long draftId) throws CustomException;
	
	/**
	 * 查询待审核的内容
	 * @param param
	 * @return
	 * @throws CustomException
	 */
	ContentVO auditContent(AuditParam param) throws CustomException;
}
