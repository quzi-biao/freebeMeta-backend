package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.ProjectMemberParam;
import com.freebe.code.business.meta.controller.param.ProjectMemberQueryParam;
import com.freebe.code.business.meta.controller.param.ProjectParam;
import com.freebe.code.business.meta.controller.param.ProjectQueryParam;
import com.freebe.code.business.meta.entity.Project;
import com.freebe.code.business.meta.repository.ProjectRepository;
import com.freebe.code.business.meta.service.BountyService;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.service.ProjectMemberService;
import com.freebe.code.business.meta.service.ProjectRecordService;
import com.freebe.code.business.meta.service.ProjectService;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.service.impl.lucene.ProjectLuceneSearch;
import com.freebe.code.business.meta.type.ProjectBillState;
import com.freebe.code.business.meta.type.ProjectMemberState;
import com.freebe.code.business.meta.type.ProjectState;
import com.freebe.code.business.meta.type.ProjectType;
import com.freebe.code.business.meta.type.WorkState;
import com.freebe.code.business.meta.vo.MemberVO;
import com.freebe.code.business.meta.vo.ProjectMemberVO;
import com.freebe.code.business.meta.vo.ProjectTag;
import com.freebe.code.business.meta.vo.ProjectVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project> implements ProjectService {
	public static final int MAX_NAME_LENGTH = 64;
	
	public static final int MAX_DESCRIPTION_LENGTH = 5000;
	
	@Autowired
	private ProjectRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ProjectMemberService projectMemberService;
	
	@Autowired
	private ProjectLuceneSearch searcher;
	
	@Autowired
	private ProjectRecordService projectRecordService;
	
	@Autowired
	private BountyService bountyService;
	
	@Autowired
	private TransactionService transactionService;

	@Override
	public ProjectVO findById(Long id) throws CustomException {
		if(null == id) {
			return null;
		}
		Project ret = getEntity(id);
		return toVO(ret, true);
	}

	
	@Override
	public Long getOwnerId(Long projectId) {
		Project ret = getEntity(projectId);
		return ret.getOwnerId();
	}

	@Override
	public ProjectVO createOrUpdate(ProjectParam param) throws CustomException {
		checkParam(param);
		
		Project e = this.getUpdateEntity(param);
		
		Long currUser = this.getCurrentUser().getId();
		e.setOwnerId(currUser);
		Double preAmount = param.getPreAmount();
		Double totalMemberPreAmount = 0D;
		for(ProjectMemberParam pm : param.getMembers()) {
			totalMemberPreAmount += pm.getPreAmount();
		}
		if(null == preAmount) {
			preAmount = totalMemberPreAmount;
		}else {
			if(preAmount.doubleValue() < totalMemberPreAmount.doubleValue()) {
				preAmount = totalMemberPreAmount;
			}
		}
		
		boolean create = true;
		if(param.getId() == null) {
			e.setState(ProjectState.INVITING);
			e.setBillState(ProjectBillState.DONE);
		}else {
			create = false;
			int state = e.getState();
			if(param.getProjectType() != ProjectType.PUBLIC && e.getProjectType() == ProjectType.PUBLIC) {
				throw new CustomException("公共项目的类型不可修改为其他类型的项目");
			}
			
			// 改变项目类型
			if(param.getProjectType() == ProjectType.PUBLIC && e.getProjectType() != ProjectType.PUBLIC ) {
				e.setState(ProjectState.RUNNING);
				e.setBillState(ProjectBillState.DONE);
				// 清空成员信息
				param.setMembers(new ArrayList<>());
				this.projectMemberService.updateProjectMembers(e.getId(), param.getMembers());
			}else {
				if(!(state == ProjectState.CREATING || state == ProjectState.INVITING ||
						state == ProjectState.WAIT_START || state == ProjectState.RUNNING)) {
					throw new CustomException("项目已结束，不可修改");
				}
			}
		}
		
		e.setPreAmount(currencyStr(preAmount));
		e.setRealAmount(currencyStr(param.getRealAmount()));
		e.setProjectType(param.getProjectType());		
		e.setCurrency(param.getCurrency());
		e.setPictures(toStr(param.getPictures()));
		e.setTags(toStr(param.getTags()));
		e.setDescription(param.getDescription());
		e.setBountyTakeLimit(param.getBountyTakeLimit());
		e.setBountyCreateLimit(param.getBountyCreateLimit());		
		
		e = repository.save(e);
		
		this.projectMemberService.updateProjectMembers(e.getId(), param.getMembers());

		ProjectVO vo = toVO(e);
		objectCaches.put(e.getId(), e);
		searcher.addOrUpdateIndex(vo);
		
		if(create) {
			this.projectRecordService.addRecord(e.getId(), "创建项目");
		}else {
			this.projectRecordService.addRecord(e.getId(), "更新项目");
		}

		return vo;
	}
	
	@Override
	public Page<ProjectVO> queryMemberPage(ProjectMemberQueryParam param) throws CustomException {
		Page<ProjectMemberVO> pmPage = this.projectMemberService.queryPage(param);
		List<ProjectVO> retList = new ArrayList<>();

		for(ProjectMemberVO e:  pmPage.getContent()) {
			ProjectVO project = this.findById(e.getProjectId());
			if(null == project) {
				continue;
			}
			retList.add(project);
		}
		
		return new PageImpl<ProjectVO>(retList, pmPage.getPageable(), pmPage.getTotalElements());
	}

	@Override
	public Page<ProjectVO> queryPage(ProjectQueryParam param) throws CustomException {
		if(null == param.getKeyWords() || param.getKeyWords().length() == 0) {
			if(param.getKeyWords().length() > 256) {
				throw new CustomException("查询关键词太长");
			}
			if(null == param.getOrder()) {
				param.setOrder("id");
			}
			
			List<Order> orders = null;
			if(param.getDesc() == null || param.getDesc()) {
				orders = Arrays.asList(new Order[] {Order.asc("state"), Order.desc(param.getOrder())});
			}else {
				orders = Arrays.asList(new Order[] {Order.asc("state"), Order.asc(param.getOrder())});
			}
			
			PageRequest request = PageRequest.of((int)param.getCurrPage(), (int)param.getLimit(), Sort.by(orders));
			
			Specification<Project> example = buildSpec(param);

			Page<Project> page = repository.findAll(example, request);
			List<ProjectVO> retList = new ArrayList<>();

			for(Project e:  page.getContent()) {
				retList.add(toVO(e));
			}
			return new PageImpl<ProjectVO>(retList, page.getPageable(), page.getTotalElements());
		}else {
			Page<ProjectVO> page = searcher.fullTextSearch(param);
			List<ProjectVO> retList = new ArrayList<>();
			for(ProjectVO e:  page.getContent()) {
				retList.add(toVO(this.getById(e.getId())));
			}
			return new PageImpl<ProjectVO>(retList, page.getPageable(), page.getTotalElements());
		}
	}
	
	@Override
	public void softDelete(Long id) throws CustomException {
		searcher.deleteIndex(this.findById(id));
		objectCaches.delete(id, Project.class);
		super.softDelete(id);
	}

	@Override
	public ProjectVO startProject(Long projectId) throws CustomException {
		Project project = getProject(projectId);
		
		if(project.getState().intValue() != ProjectState.INVITING 
				&& project.getState().intValue() != ProjectState.WAIT_START
				&& project.getState().intValue() != ProjectState.CREATING) {
			throw new CustomException("项目状态异常: " + project.getState());
		}
		
		List<ProjectMemberVO> members = projectMemberService.getProjectMembers(projectId);
		
		if(null == members || members.size() == 0) {
			// 任何项目都必须有两个人以上(至少一名非 owner 的成员)
			throw new CustomException("请添加项目成员");
		}
		
		for(ProjectMemberVO member : members) {
			if(member.getProjectMemberState().intValue() == ProjectMemberState.WAIT_ACCEPT) {
				throw new CustomException("有成员未接受邀请，项目不能开始");
			}
		}
		
		project.setState(ProjectState.RUNNING);
		project.setStartTime(System.currentTimeMillis());
		project = this.repository.save(project);
		objectCaches.put(project.getId(), project);
		
		this.projectRecordService.addRecord(project.getId(), "项目启动");
		
		return toVO(project);
	}
	

	@Override
	public void memberAccept(Long projectId) throws CustomException {
		Project project = getProject(projectId);

		List<ProjectMemberVO> members = projectMemberService.getProjectMembers(projectId);
		
		if(null == members || members.size() == 0) {
			// 任何项目都必须有两个人以上(至少一名非 owner 的成员)
			throw new CustomException("请添加项目成员");
		}
		
		for(ProjectMemberVO member : members) {
			if(member.getProjectMemberState().intValue() != ProjectMemberState.NORMAL) {
				return;
			}
		}
		
		project.setState(ProjectState.WAIT_START);
		project.setStartTime(System.currentTimeMillis());
		project = this.repository.save(project);
		
		objectCaches.put(project.getId(), project);
	}

	@Override
	public ProjectVO doneProject(Long projectId) throws CustomException {
		Project project = getProject(projectId);
		
		if(project.getState().intValue() != ProjectState.RUNNING) {
			throw new CustomException("项目状态异常: " + project.getState());
		}
		
		List<ProjectMemberVO> members = projectMemberService.getProjectMembers(projectId);
		
		if(null == members || members.size() == 0) {
			// 任何项目都必须有两个人以上(至少一名非 owner 的成员)
			throw new CustomException("请添加项目成员");
		}
		
		for(ProjectMemberVO member : members) {
			if(member.getWorkState().intValue() == WorkState.WORKING) {
				throw new CustomException("有成员还未完成悬赏，项目不能结束");
			}
		}
		
		project.setState(ProjectState.DONE);
		project.setStartTime(System.currentTimeMillis());
		project = this.repository.save(project);
		
		objectCaches.put(project.getId(), project);
		
		this.projectRecordService.addRecord(project.getId(), "项目完成");
		
		return toVO(project);
	}

	@Override
	public ProjectVO startBill(Long projectId) throws CustomException {
		Project project = getProject(projectId);
		
		if(project.getState().intValue() != ProjectState.DONE) {
			throw new CustomException("项目未结束，不能结算");
		}
		
		project.setBillState(ProjectBillState.BILLING);
		project.setBillTime(System.currentTimeMillis());		
		project = this.repository.save(project);
		
		objectCaches.put(project.getId(), project);
		
		this.projectRecordService.addRecord(project.getId(), "项目开始结算");
		
		return toVO(project);
	}

	@Override
	public ProjectVO billEnd(Long projectId) throws CustomException {
		Project project = getProject(projectId);
		
		List<ProjectMemberVO> members = projectMemberService.getProjectMembers(projectId);
		
		if(null == members || members.size() == 0) {
			// 任何项目都必须有两个人以上(至少一名非 owner 的成员)
			throw new CustomException("请添加项目成员");
		}
		
		for(ProjectMemberVO member : members) {
			if(member.getBillState() == ProjectBillState.BILLING || member.getBillState() == ProjectBillState.NONE) {
				return toVO(project);
			}
			if(member.getBillState() == ProjectBillState.FAILED) {
				// 结算异常
				project.setState(ProjectState.BILLING_FAILED);
				project.setBillState(ProjectBillState.FAILED);		
				this.repository.save(project);
				objectCaches.put(project.getId(), project);
				
				return toVO(project);
			}
		}
		
		project.setState(ProjectState.NORMAL_END);
		project.setBillState(ProjectBillState.DONE);		
		project.setEndTime(System.currentTimeMillis());
		project = this.repository.save(project);
		
		objectCaches.put(project.getId(), project);
		
		this.projectRecordService.addRecord(project.getId(), "项目结算完成");
		
		return toVO(project);
	}

	private Specification<Project> buildSpec(ProjectQueryParam param) throws CustomException {
		return new Specification<Project>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Project> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("billState", param.getBillState());
				builder.addEqual("state", param.getState());
				builder.addEqual("projectType", param.getProjectType());

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}
	
	private ProjectVO toVO(Project e) throws CustomException {
		return toVO(e, false);
	}
	

	private ProjectVO toVO(Project e, boolean isDetail) throws CustomException {
		if(null == e) {
			return null;
		}
		ProjectVO vo = new ProjectVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setOwnerId(e.getOwnerId());
		vo.setOwner(userService.getUser(e.getOwnerId()));
		
		vo.setPreAmount(numbericCurrency(e.getPreAmount()));
		vo.setRealAmount(numbericCurrency(e.getPreAmount()));
		
		vo.setCurrency(e.getCurrency());
		vo.setPictures(toList(e.getPictures()));
		vo.setState(e.getState());
		vo.setTags(toList(e.getTags(), ProjectTag.class));
		vo.setDescription(e.getDescription());
		vo.setStartTime(e.getStartTime());
		vo.setDoneTime(e.getDoneTime());
		vo.setEndTime(e.getEndTime());
		vo.setBillTime(e.getBillTime());
		vo.setBillState(e.getBillState());
		vo.setProjectType(e.getProjectType());
		vo.setBountyCreateLimit(e.getBountyCreateLimit());
		vo.setBountyTakeLimit(e.getBountyTakeLimit());
		
		if(isDetail) {
			vo.setProjectReward(this.transactionService.getProjectReward(e.getId()));
			vo.setCostAmount(this.bountyService.countCost(e.getId()));
		}
		
		vo.setMembers(this.projectMemberService.getProjectMembers(e.getId()));
		return vo;
	}

	private Project getProject(Long projectId) throws CustomException {
		Project project = this.getReference(projectId);
		if(null == project) {
			throw new CustomException("项目不存在");
		}
		
		if(project.getOwnerId().longValue() != project.getOwnerId().longValue()) {
			throw new CustomException("请无权执行此操作，请联系项目管理员");
		}
		return project;
	}
	

	@Override
	public Integer getProjectState(Long projectId) throws CustomException {
		Project ret = this.objectCaches.get(projectId, Project.class);
		if(null == ret){
			Optional<Project> op = this.repository.findById(projectId);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		if(null == ret) {
			throw new CustomException("项目不存在");
		}
		return ret.getState();
	}
	
	private void checkParam(ProjectParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getName())) {
			throw new CustomException("项目名称不能为空");
		}
		
		if(StringUtils.isEmpty(param.getDescription())) {
			throw new CustomException("项目介绍不能为空");
		}
		
		if(null == param.getPictures() || param.getPictures().size() == 0){
			throw new CustomException("项目图片不能为空");
		}else {
			if(StringUtils.isEmpty(param.getPictures().get(0))) {
				throw new CustomException("项目图片不能为空");
			}
		}
		
		if(null == param.getTags() || param.getTags().size() == 0) {
			throw new CustomException("项目标签不能为空");
		}else {
			if(param.getTags().size() > 16) {
				throw new CustomException("项目标签不能超过16个");
			}
			for(ProjectTag tag : param.getTags()) {
				if(null == tag || StringUtils.isEmpty(tag.getName())) {
					throw new CustomException("项目标签内容不能为空");
				}
				if(tag.getName().length() > 12) {
					throw new CustomException("项目标签不能超过12个字");
				}
			}
		}
		
//		if(param.getProjectType() != ProjectType.PUBLIC) {
//			if(null == param.getMembers() || param.getMembers().size() == 0){
//				throw new CustomException("至少要有一名项目成员");
//			}
//		}
		
		if(param.getName().length() > MAX_NAME_LENGTH) {
			throw new CustomException("项目名称不能超过" + MAX_NAME_LENGTH + "个字符");
		}
		
		if(param.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
			throw new CustomException("项目描述不能超过" + MAX_DESCRIPTION_LENGTH + "个字符");
		}
		
		Long currUser = this.getCurrentUser().getId();
		MemberVO owner = this.memberService.findByUserId(currUser);
		if(null == owner) {
			throw new CustomException("请您先填写个人信息");
		}
		
		boolean hasOwner = false;
		for(ProjectMemberParam pm : param.getMembers()) {
			if(null == pm.getPreAmount()) {
				throw new CustomException("请为成员设置预算积分");
			}
			if(null == pm.getRole()) {
				throw new CustomException("请为成员设置项目角色");
			}
			if(pm.getMemberId().longValue() == owner.getId().longValue()) {
				hasOwner = true;
			}
		}
		
		if(!hasOwner) {
			ProjectMemberParam pm = new ProjectMemberParam();
			pm.setMemberId(owner.getId());
			pm.setRole("项目主理人");
			pm.setPreAmount(0D);
			param.getMembers().add(pm);
		}
	}
	

	private Project getEntity(Long id) {
		Project ret = this.objectCaches.get(id, Project.class);
		if(null == ret){
			Optional<Project> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

}
