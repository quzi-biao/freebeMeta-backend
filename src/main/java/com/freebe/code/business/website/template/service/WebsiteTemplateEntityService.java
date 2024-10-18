package com.freebe.code.business.website.template.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.website.template.controller.param.WebsiteTemplateEntityParam;
import com.freebe.code.business.website.template.controller.param.WebsiteTemplateEntityQueryParam;
import com.freebe.code.business.website.template.entity.WebsiteTemplateEntity;
import com.freebe.code.business.website.template.vo.WebsiteTemplateEntityVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface WebsiteTemplateEntityService extends BaseService<WebsiteTemplateEntity> {

	WebsiteTemplateEntityVO findById(Long id) throws CustomException;

	WebsiteTemplateEntityVO createOrUpdate(WebsiteTemplateEntityParam param) throws CustomException;

	Page<WebsiteTemplateEntityVO> queryPage(WebsiteTemplateEntityQueryParam param) throws CustomException;

}
