package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.ContentData;
import com.freebe.code.business.meta.vo.ContentDataVO;
import com.freebe.code.business.meta.controller.param.ContentDataParam;
import com.freebe.code.business.meta.controller.param.ContentDataQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface ContentDataService extends BaseService<ContentData> {

	ContentDataVO findById(Long id) throws CustomException;
	
	String findByKey(String key) throws CustomException;
	
	/**
	 * 更新内容
	 * @param key
	 * @param content
	 * @return
	 * @throws CustomException
	 */
	String updateContent(String key, String content, Integer type) throws CustomException;

	String createOrUpdate(ContentDataParam param) throws CustomException;
	
	Page<ContentDataVO> queryPage(ContentDataQueryParam param) throws CustomException;
}
