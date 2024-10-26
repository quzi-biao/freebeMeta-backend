package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.RichTextParam;
import com.freebe.code.business.meta.controller.param.RichTextQueryParam;
import com.freebe.code.business.meta.controller.param.RichTextUpdateParam;
import com.freebe.code.business.meta.entity.RichText;
import com.freebe.code.business.meta.vo.RichTextVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface RichTextService extends BaseService<RichText> {

	RichTextVO findByDocId(String docId) throws CustomException;

	String createOrUpdate(RichTextParam param) throws CustomException;
	
	RichTextVO updateContent(RichTextUpdateParam param) throws CustomException;

	Page<RichTextVO> queryPage(RichTextQueryParam param) throws CustomException;

}
