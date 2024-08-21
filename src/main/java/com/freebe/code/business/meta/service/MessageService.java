package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.Message;
import com.freebe.code.business.meta.vo.MessageVO;
import com.freebe.code.business.meta.controller.param.MessageParam;
import com.freebe.code.business.meta.controller.param.MessageQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface MessageService extends BaseService<Message> {

	MessageVO createOrUpdate(MessageParam param) throws CustomException;

	Page<MessageVO> queryPage(MessageQueryParam param) throws CustomException;
	
	/**
	 * 统计当前用户的未读消息数量
	 * @return
	 * @throws CustomException
	 */
	Long countUnRead() throws CustomException;

	/**
	 * 读消息
	 * @param msgId
	 * @return
	 * @throws CustomException
	 */
	MessageVO readMsg(Long msgId, Object params) throws CustomException;
}
