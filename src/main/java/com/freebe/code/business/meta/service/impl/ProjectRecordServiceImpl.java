package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.business.meta.controller.param.ProjectRecordParam;
import com.freebe.code.business.meta.controller.param.ProjectRecordQueryParam;
import com.freebe.code.business.meta.entity.Project;
import com.freebe.code.business.meta.entity.ProjectMember;
import com.freebe.code.business.meta.entity.ProjectRecord;
import com.freebe.code.business.meta.repository.ProjectRecordRepository;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.service.ProjectMemberService;
import com.freebe.code.business.meta.service.ProjectRecordService;
import com.freebe.code.business.meta.service.ProjectService;
import com.freebe.code.business.meta.type.ProjectState;
import com.freebe.code.business.meta.type.ProjectType;
import com.freebe.code.business.meta.type.RecordType;
import com.freebe.code.business.meta.vo.ProjectRecordVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class ProjectRecordServiceImpl extends BaseServiceImpl<ProjectRecord> implements ProjectRecordService {
	@Autowired
	private ProjectRecordRepository repository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectMemberService projectMemberService;

	@Override
	public ProjectRecordVO findById(Long id) throws CustomException {
		Optional<ProjectRecord> op = this.repository.findById(id);
		if(!op.isPresent()){
			return null;
		}
		return toVO(op.get());
	}

	@Override
	public ProjectRecordVO createOrUpdate(ProjectRecordParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getContent()) && CollectionUtils.isEmpty(param.getPictures())
				&& CollectionUtils.isEmpty(param.getAppendFiles())) {
			throw new CustomException("请输入内容");
		}
		
		if(null == param.getProjectId()) {
			throw new CustomException("请设置评论的项目");
		}
		
		Project project = this.projectService.getReference(param.getProjectId().longValue());
		if(project.getState() == ProjectState.NORMAL_END) {
			throw new CustomException("项目已结束, 不能再添加项目记录");
		}
		
		if(project.getProjectType() != ProjectType.BUSINESS) {
			Long memberId = this.memberService.getMemberIdByUserId(getCurrentUser().getId());
			ProjectMember pm = this.projectMemberService.findOne(param.getProjectId(), memberId);
			if(null == pm) {
				throw new CustomException("只有项目组成员才可以添加项目记录");
			}
			param.setProjectRole(pm.getRole());
		}
		
		if(null == param.getRecordType()) {
			param.setRecordType(RecordType.NORMAL);
		}
		
		ProjectRecord e = this.innerCreateOrUpdate(param);

		ProjectRecordVO vo = toVO(e);
		return vo;
	}
	
	@Override
	public ProjectRecord innerCreateOrUpdate(ProjectRecordParam param) throws CustomException {
		ProjectRecord e = this.getUpdateEntity(param, false);
		
		if(null == param.getUserId()) {
			UserVO user = this.getCurrentUser();
			if(null == user) {
				// 系统默认的用户ID
				e.setUserId(1L);
			}else {
				e.setUserId(user.getId());
				if(null == param.getProjectRole()) {
					Long memberId = this.memberService.getMemberIdByUserId(getCurrentUser().getId());
					ProjectMember pm = this.projectMemberService.findOne(param.getProjectId(), memberId);
					if(null != pm) {
						param.setProjectRole(pm.getRole());
					}
				}
				
			}
		}
		
		e.setFrontRecordId(param.getFrontRecordId());
		e.setFrontUserId(param.getFrontUserId());
		e.setContent(param.getContent());
		e.setAppendFiles(toStr(param.getAppendFiles()));
		e.setPictures(toStr(param.getPictures()));
		e.setProjectId(param.getProjectId());
		e.setReocrdType(param.getRecordType());
		e.setProjectRole(param.getProjectRole());
		
		e = repository.save(e);
		
		return e;
	}
	
	@Override
	public ProjectRecord addRecord(Long projectId, String content) throws CustomException {
		ProjectRecordParam param = new ProjectRecordParam();
		param.setProjectId(projectId);
		param.setContent(content);
		param.setRecordType(RecordType.SYSTEM);
		
		return this.innerCreateOrUpdate(param);
	}

	@Override
	public Page<ProjectRecordVO> queryPage(ProjectRecordQueryParam param) throws CustomException {
		if(null == param.getProjectId()) {
			throw new CustomException("请设置查询的项目");
		}
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<ProjectRecord> example = buildSpec(param);

		Page<ProjectRecord> page = repository.findAll(example, request);
		List<ProjectRecordVO> retList = new ArrayList<>();

		for(ProjectRecord e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<ProjectRecordVO>(retList, page.getPageable(), page.getTotalElements());
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		ProjectRecordVO pr = this.findById(id);
		if(null == pr) {
			return;
		}
		
		if(pr.getUserId() != getCurrentUser().getId()) {
			throw new CustomException("您不能执行此操作");
		}
		
		super.softDelete(id);
	}

	private Specification<ProjectRecord> buildSpec(ProjectRecordQueryParam param) throws CustomException {
		return new Specification<ProjectRecord>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ProjectRecord> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<ProjectRecord> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addEqual("projectId", param.getProjectId());

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private ProjectRecordVO toVO(ProjectRecord e) throws CustomException {
		ProjectRecordVO vo = new ProjectRecordVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setUserId(e.getUserId());
		vo.setUser(userService.getUser(vo.getUserId()));
		vo.setFrontRecordId(e.getFrontRecordId());
		vo.setFrontUserId(e.getFrontUserId());
		vo.setContent(e.getContent());
		vo.setAppendFiles(toList(e.getAppendFiles(), String.class));
		vo.setProjectId(e.getProjectId());
		vo.setPictures(toList(e.getPictures(), String.class));
		vo.setProjectRole(e.getProjectRole());
		vo.setRecordType(e.getReocrdType());
		
		return vo;
	}

}
