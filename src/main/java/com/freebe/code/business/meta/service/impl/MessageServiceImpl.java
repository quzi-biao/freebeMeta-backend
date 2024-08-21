package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.MessageParam;
import com.freebe.code.business.meta.controller.param.MessageQueryParam;
import com.freebe.code.business.meta.entity.Message;
import com.freebe.code.business.meta.repository.MessageRepository;
import com.freebe.code.business.meta.service.MessageService;
import com.freebe.code.business.meta.service.ProjectMemberService;
import com.freebe.code.business.meta.type.MessageState;
import com.freebe.code.business.meta.type.MessageType;
import com.freebe.code.business.meta.vo.MessageVO;
import com.freebe.code.business.meta.vo.ProjectInviteMessage;
import com.freebe.code.common.CustomException;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService {
	@Autowired
	private MessageRepository repository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectMemberService projectMemberService;

	@Override
	public MessageVO createOrUpdate(MessageParam param) throws CustomException {
		Message e = this.getUpdateEntity(param, false);

		e.setSender(getCurrentUser().getId());
		e.setReciever(param.getReciever());
		e.setMessageType(param.getMessageType());
		e.setState(MessageState.UNREAD);
		e.setContent(param.getContent());

		e = repository.save(e);

		MessageVO vo = toVO(e);

		return vo;
	}
	
	@Override
	public Long countUnRead() throws CustomException {
		Long userId = getCurrentUser().getId();
		
		Message probe = new Message();
		probe.setIsDelete(false);
		probe.setReciever(userId);
		probe.setState(MessageState.UNREAD);
		
		return this.repository.count(Example.of(probe));
	}

	@Override
	public MessageVO readMsg(Long msgId, Object params) throws CustomException {
		Message msg = this.getReference(msgId);
		if(null == msg) {
			throw new CustomException("消息不存在");
		}
		Long userId = getCurrentUser().getId();
		if(userId.longValue() != msg.getReciever().longValue()) {
			throw new CustomException("您无此操作权限");
		}
		
		msg.setState(MessageState.READ);
		msg.setReadTime(System.currentTimeMillis());
		
		if(null != msg.getMessageType() && msg.getMessageType().intValue() == MessageType.PROJECT_INVITE) {
			handleProjectInvite(msg.getContent(), params);
		}
		
		this.repository.save(msg);
		
		return toVO(msg);
	}

	@Override
	public Page<MessageVO> queryPage(MessageQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<Message> example = buildSpec(param);

		Page<Message> page = repository.findAll(example, request);
		List<MessageVO> retList = new ArrayList<>();

		for(Message e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<MessageVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<Message> buildSpec(MessageQueryParam param) throws CustomException {
		return new Specification<Message>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Message> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("sender", param.getSender());
				builder.addEqual("reciever", param.getReciever());
				builder.addEqual("messageType", param.getMessageType());
				builder.addEqual("state", param.getState());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private MessageVO toVO(Message e) throws CustomException {
		MessageVO vo = new MessageVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setSender(userService.getUser(e.getSender()));
		vo.setReciever(userService.getUser(e.getReciever()));
		vo.setMessageType(e.getMessageType());
		vo.setState(e.getState());
		vo.setContent(e.getContent());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		super.softDelete(id);
	}

	/**
	 * 处理项目邀请的信息
	 * @param content
	 * @param params
	 * @throws CustomException 
	 */
	private void handleProjectInvite(String content, Object params) throws CustomException {
		ProjectInviteMessage message = JSONObject.parseObject(content, ProjectInviteMessage.class);
		if(null != params) {
			// 取值为 1 时表示同意
			if(params.toString().equals("1")) {
				this.projectMemberService.accept(message.getProjectId());
			}else {
				this.projectMemberService.reject(message.getProjectId());
			}
		}else {
			this.projectMemberService.reject(message.getProjectId());
		}
		
	}

}
