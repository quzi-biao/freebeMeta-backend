package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.MessageParam;
import com.freebe.code.business.meta.controller.param.ProjectMemberBillParam;
import com.freebe.code.business.meta.controller.param.ProjectMemberParam;
import com.freebe.code.business.meta.controller.param.ProjectMemberQueryParam;
import com.freebe.code.business.meta.controller.param.TransactionParam;
import com.freebe.code.business.meta.entity.ProjectMember;
import com.freebe.code.business.meta.repository.ProjectMemberRepository;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.service.MessageService;
import com.freebe.code.business.meta.service.ProjectMemberService;
import com.freebe.code.business.meta.service.ProjectRecordService;
import com.freebe.code.business.meta.service.ProjectService;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.service.WalletService;
import com.freebe.code.business.meta.type.Currency;
import com.freebe.code.business.meta.type.MessageType;
import com.freebe.code.business.meta.type.ProjectBillState;
import com.freebe.code.business.meta.type.ProjectMemberState;
import com.freebe.code.business.meta.type.ProjectState;
import com.freebe.code.business.meta.type.TransactionState;
import com.freebe.code.business.meta.type.TransactionType;
import com.freebe.code.business.meta.type.WorkState;
import com.freebe.code.business.meta.vo.MemberVO;
import com.freebe.code.business.meta.vo.ProjectInviteMessage;
import com.freebe.code.business.meta.vo.ProjectMemberVO;
import com.freebe.code.business.meta.vo.ProjectVO;
import com.freebe.code.business.meta.vo.TransactionVO;
import com.freebe.code.business.meta.vo.WalletVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class ProjectMemberServiceImpl extends BaseServiceImpl<ProjectMember> implements ProjectMemberService {
	@Autowired
	private ProjectMemberRepository repository;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private ProjectRecordService projectRecordService;

	@Override
	public ProjectMemberVO findById(Long id) throws CustomException {
		Optional<ProjectMember> op = this.repository.findById(id);
		if(!op.isPresent()){
			return null;
		}
		return toVO(op.get());
	}
	
	@Override
	public List<ProjectMemberVO> getProjectMembers(Long projectId) throws CustomException {
		if(null == projectId) {
			return null;
		}
		ProjectMember probe = new ProjectMember();
		
		probe.setProjectId(projectId);
		probe.setIsDelete(false);
		
		List<ProjectMember> results = this.repository.findAll(Example.of(probe));
		
		List<ProjectMemberVO> ret = new ArrayList<>();
		
		for(ProjectMember pm : results) {
			ret.add(toVO(pm));
		}
		
		return ret;
	}
	
	@Override
	public void updateProjectMembers(Long projectId, List<ProjectMemberParam> members) throws CustomException {
		if(null == projectId || null == members || members.size() == 0) {
			return;
		}
		
		List<ProjectMemberVO> front = this.getProjectMembers(projectId);
		
		// 原先有，如今没有要删除
		// 如今有的都需要执行更新动作
		for(ProjectMemberParam member : members) {
			member.setProjectId(projectId);
			this.createOrUpdate(member);
		}
		
		for(ProjectMemberVO vo : front) {
			boolean find = false;
			for(ProjectMemberParam member : members) {
				if(member.getMemberId().longValue() == vo.getMemberId().longValue()) {
					find = true;
				}
			}
			if(!find) {
				this.softDelete(vo.getId());
			}
		}
	}

	@Override
	public ProjectMemberVO createOrUpdate(ProjectMemberParam param) throws CustomException {
		ProjectMember e = this.findOne(param.getProjectId(), param.getMemberId());
		MemberVO member = this.memberService.findById(param.getMemberId());
		
		if(null == e) {
			e = this.getUpdateEntity(param, false);
			e.setProjectId(param.getProjectId());
			e.setMemberId(param.getMemberId());
			e.setProjectMemberState(ProjectMemberState.WAIT_ACCEPT);
			e.setWorkState(WorkState.WORKING);
			e.setBillState(ProjectBillState.NONE);
			
			ProjectVO project = this.projectService.findById(param.getProjectId());
			if(member.getUserId().longValue() != project.getOwnerId().longValue()) {
				// 如果是新成员，发送邀请消息
				MessageParam msgParam = new MessageParam();
				msgParam.setMessageType(MessageType.PROJECT_INVITE);
				msgParam.setReciever(memberService.findById(param.getMemberId()).getUserId());
				msgParam.setSender(project.getOwnerId());
				ProjectInviteMessage msg = new ProjectInviteMessage();
				msg.setProjectId(param.getProjectId());
				msg.setMemberId(param.getMemberId());
				msg.setOwnerName(project.getOwner().getName());
				msg.setProjectName(project.getName());
				
				msgParam.setContent(JSONObject.toJSONString(msg));		
				
				messageService.createOrUpdate(msgParam);
			}else {
				e.setProjectMemberState(ProjectMemberState.NORMAL);
			}
			this.addUpdateRecord(false, param.getProjectId(), member.getName(), param.getPreAmount(), param.getRole());
		}else {
			if(e.getRole().equals(param.getRole()) && e.getPreAmount().equals(currencyStr(param.getPreAmount()))) {
				return toVO(e);
			}
			this.addUpdateRecord(true, param.getProjectId(), member.getName(), param.getPreAmount(), param.getRole());
		}
		
		e.setRole(param.getRole());
		e.setPreAmount(currencyStr(param.getPreAmount()));

		e = repository.save(e);

		ProjectMemberVO vo = toVO(e);

		return vo;
	}

	@Override
	public Page<ProjectMemberVO> queryPage(ProjectMemberQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<ProjectMember> example = buildSpec(param);

		Page<ProjectMember> page = repository.findAll(example, request);
		List<ProjectMemberVO> retList = new ArrayList<>();

		for(ProjectMember e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<ProjectMemberVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<ProjectMember> buildSpec(ProjectMemberQueryParam param) throws CustomException {
		return new Specification<ProjectMember>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ProjectMember> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<ProjectMember> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("memberId", param.getMemberId());
				builder.addEqual("projectId", param.getProjectId());
				builder.addLike("role", param.getRole());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	@Transactional
	@Override
	public ProjectMemberVO bill(ProjectMemberBillParam billParam) throws CustomException {
		if(null == billParam.getAmount()) {
			throw new CustomException("请输入结算金额");
		}
		if(StringUtils.isEmpty(billParam.getEvalute())) {
			throw new CustomException("请输入评价及结算缘由");
		}
		if(billParam.getEvalute().length() > 5000) {
			throw new CustomException("评价不能超过5000字");
		}
		if(null == billParam.getProjectId() || billParam.getProjectId() == 0) {
			throw new CustomException("请输入项目ID");
		}
		if(null == billParam.getMemberId() || billParam.getMemberId() == 0) {
			throw new CustomException("请输入成员ID");
		}
		
		Long currUserId = this.getCurrentUser().getId();
		ProjectVO project = this.projectService.findById(billParam.getProjectId());
		
		if(null == project) {
			throw new CustomException("项目不存在");
		}
		
		if(project.getState() != ProjectState.DONE) {
			throw new CustomException("项目不可结算");
		}

		if(project.getOwnerId().longValue() != currUserId.longValue()) {
			throw new CustomException("请不要随意发放积分");
		}
		
		ProjectMember pm = this.findOne(billParam.getProjectId(), billParam.getMemberId());
		if(null == pm) {
			throw new CustomException("不能发放给非本项目的成员");
		}
		
		MemberVO member = this.memberService.findById(billParam.getMemberId());
		if(null == member) {
			throw new CustomException("不能发放给非本社区的成员");
		}
		
		// 创建交易
		TransactionParam param = new TransactionParam();
		param.setAmount(billParam.getAmount());
		param.setCurrency(project.getCurrency());
		param.setSrcWalletId(1L);
		if(null != pm.getTransactionId()) {
			TransactionVO vo = this.transactionService.findById(pm.getTransactionId());
			int state = vo.getState().intValue();
			if(state == TransactionState.CONFIRM || state == TransactionState.CONFIRMING || state == TransactionState.PUBLICITY) {
				throw new CustomException("公示中不可修改");
			}
			
			if(state != TransactionState.FAILED && state != TransactionState.CANCEL) {
				param.setId(pm.getTransactionId());
			}
		}
		
		WalletVO wallet = this.walletService.findByUser(member.getUserId());
		param.setDstWalletId(wallet.getId());
		param.setAmount(billParam.getAmount());
		param.setMark(billParam.getEvalute());
		if(null == param.getCurrency()) {
			param.setCurrency(Currency.FREE_BE);
		}
		param.setTransactionType(TransactionType.FREEBE_DISTRIBUTE);
		param.setProjectId(billParam.getProjectId());
		
		TransactionVO transaction = this.transactionService.innerCreateOrUpdate(param);
		
		pm.setBillState(ProjectBillState.BILLING);
		pm.setRealAmount(currencyStr(billParam.getAmount()));
		pm.setTransactionId(transaction.getId());
		pm.setBillTime(System.currentTimeMillis());
		pm.setEvaluation(billParam.getEvalute());
		
		pm = this.repository.save(pm);
		
		if(project.getBillState() != ProjectBillState.BILLING) {
			this.projectService.startBill(pm.getProjectId());
		}
		
		this.addStartBillRecord(pm, member.getName(), billParam.getAmount(), pm.getEvaluation());
		
		return toVO(pm);
	}

	@Override
	public ProjectMemberVO doneWork(Long projectMemberId) throws CustomException {
		ProjectMemberVO vo = this.findById(projectMemberId);
		ProjectMember pm = getCurr(vo.getProjectId());
		if(pm.getMemberId().longValue() != vo.getMemberId()) {
			throw new CustomException("您不能为其他成员提交完成悬赏");
		}
		
		if(pm.getProjectMemberState().intValue() != ProjectMemberState.NORMAL) {
			throw new CustomException("您还不是项目成员");
		}
		
		ProjectVO project = this.projectService.findById(pm.getProjectId());
		if(project.getState() == ProjectState.CREATING 
				|| project.getState() == ProjectState.WAIT_START
				|| project.getState() == ProjectState.INVITING) {
			throw new CustomException("项目还未开始");
		}
		
		pm.setWorkState(WorkState.DONE);
		pm.setDoneTime(System.currentTimeMillis());
		
		this.projectRecordService.addRecord(pm.getProjectId(), "完成了悬赏");	
		return toVO(pm);
	}
	
	@Override
	public ProjectMemberVO reject(Long projectId) throws CustomException {
		ProjectMember pm = this.getCurr(projectId);
		
		if(pm.getProjectMemberState().intValue() != ProjectMemberState.WAIT_ACCEPT) {
			throw new CustomException("状态异常");
		}
		
		pm.setJoinTime(System.currentTimeMillis());
		pm.setProjectMemberState(ProjectMemberState.REJECT);
		this.projectRecordService.addRecord(pm.getProjectId(), "拒绝了您的项目邀请");	
		return toVO(pm);
	}

	@Override
	public ProjectMemberVO accept(Long projectId) throws CustomException {
		ProjectMember pm = getCurr(projectId);
		
		if(pm.getProjectMemberState().intValue() != ProjectMemberState.WAIT_ACCEPT) {
			throw new CustomException("状态异常");
		}
		
		pm.setJoinTime(System.currentTimeMillis());
		pm.setProjectMemberState(ProjectMemberState.NORMAL);
		
		this.projectService.memberAccept(projectId);
		this.projectRecordService.addRecord(pm.getId(), "接受项目邀请");
		
		return toVO(pm);
	}

	@Override
	public ProjectMemberVO dismission(Long projectId, String reason) throws CustomException {
		ProjectMember pm = getCurr(projectId);
		
		if(pm.getProjectMemberState().intValue() != ProjectMemberState.NORMAL) {
			throw new CustomException("当前状态不可离开");
		}
		
		pm.setDimissionTime(System.currentTimeMillis());
		pm.setProjectMemberState(ProjectMemberState.DISMISSION);
		pm.setDismissionReason(reason);
		
		this.projectRecordService.addRecord(pm.getProjectId(), "退出了项目");	
		
		return toVO(pm);
	}
	

	@Override
	public Integer countProject(Long memberId) throws CustomException {
		ProjectMember probe = new ProjectMember();
		
		probe.setMemberId(memberId);
		probe.setIsDelete(false);
		
		return (int) this.repository.count(Example.of(probe));
	}
	
	/**
	 * 查询
	 * @param projectId
	 * @param memberId
	 * @return
	 * @throws CustomException 
	 */
	@Override
	public ProjectMemberVO findProjectMmeber(Long projectId, Long memberId) throws CustomException {
		ProjectMember pm = this.findOne(projectId, memberId);
		if(null != pm) {
			return toVO(pm);
		}
		return null;
	}
	

	@Override
	public ProjectMemberVO billEnd(Long transactionId, Integer transactionState) throws CustomException {
		ProjectMember pm = this.findByTransactionId(transactionId);
		if(null == pm) {
			throw new CustomException("交易不存在");
		}
		
		int state = transactionState.intValue();
		if(state ==  TransactionState.FAILED || state == TransactionState.CONFIRM) {
			if(transactionState.intValue() == TransactionState.FAILED) {
				pm.setBillState(ProjectBillState.FAILED);
			}else if(transactionState.intValue() == TransactionState.CONFIRM) {
				pm.setBillState(ProjectBillState.DONE);
			}
			
			pm.setBillEndTime(System.currentTimeMillis());
			// 更新项目的结算状态
			this.projectService.billEnd(pm.getProjectId());
			this.repository.save(pm);
			MemberVO member = this.memberService.findById(pm.getMemberId());
			this.addEndBillRecord(state ==  TransactionState.CONFIRM, pm, member.getName(), numbericCurrency(pm.getRealAmount()));		
		}
		
		return toVO(pm);
	}
	
	@Override
	public void softDelete(Long id) throws CustomException {
		this.stop(id);
		super.softDelete(id);
	}

	private ProjectMemberVO toVO(ProjectMember e) throws CustomException {
		ProjectMemberVO vo = new ProjectMemberVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setProjectId(e.getProjectId());
		vo.setMemberId(e.getMemberId());
		vo.setMember(memberService.findById(e.getMemberId()));	
		
		vo.setJoinTime(e.getJoinTime());
		vo.setDimissionTime(e.getDimissionTime());
		vo.setRole(e.getRole());
		vo.setProjectMemberState(e.getProjectMemberState());
		vo.setWorkState(e.getWorkState());
		vo.setBillState(e.getBillState());
		vo.setPreAmount(numbericCurrency(e.getPreAmount()));
		vo.setRealAmount(numbericCurrency(e.getRealAmount()));
		vo.setEvaluation(e.getEvaluation());
		vo.setBillTime(e.getBillTime());
		if(null != e.getTransactionId()) {
			vo.setTransaction(transactionService.findById(e.getTransactionId()));		
		}
		return vo;
	}

	/**
	 * 
	 * @param projectId
	 * @param memberId
	 * @return
	 */
	@Override
	public ProjectMember findOne(Long projectId, Long memberId) {
		if(null == projectId || memberId == null) {
			return null;
		}
		ProjectMember probe = new ProjectMember();
		
		probe.setProjectId(projectId);
		probe.setMemberId(memberId);
		probe.setIsDelete(false);
		
		List<ProjectMember> results = this.repository.findAll(Example.of(probe));
		
		if(null == results || results.size() == 0) {
			return null;
		}
		
		return results.get(0);
	}
	
	/**
	 * 添加成员的更新记录
	 * @param update
	 * @param projectId
	 * @param memberName
	 * @param preAmount
	 * @param role
	 * @throws CustomException
	 */
	private void addEndBillRecord(boolean isSuccess, ProjectMember pm, String memberName, Double amount) throws CustomException {
		StringBuffer buf = new StringBuffer();
		
		buf.append("公示期结束: ").append(memberName).append("");
		buf.append(isSuccess ? "结算完成: ": "结算失败: ");
		buf.append("结算金额: ").append(amount).append('\n');
		
		projectRecordService.addRecord(pm.getProjectId(), buf.toString());
	}
	
	/**
	 * 添加成员的更新记录
	 * @param update
	 * @param projectId
	 * @param memberName
	 * @param preAmount
	 * @param role
	 * @throws CustomException
	 */
	private void addStartBillRecord(ProjectMember pm, String memberName, Double amount, String mark) throws CustomException {
		StringBuffer buf = new StringBuffer();
		
		boolean create = pm.getTransactionId() == null;
		buf.append("成员“").append(memberName).append("”");
		buf.append(create ? "更新结算信息: ":"开始结算: ");
		buf.append('\n');
		
		buf.append("结算金额: ").append(amount).append('\n');
		buf.append("结算说明: ").append('\n');
		buf.append(mark).append('\n');
		
		projectRecordService.addRecord(pm.getProjectId(), buf.toString());
	}
	
	/**
	 * 添加成员的更新记录
	 * @param update
	 * @param projectId
	 * @param memberName
	 * @param preAmount
	 * @param role
	 * @throws CustomException
	 */
	private void addUpdateRecord(boolean update, Long projectId, String memberName, Double preAmount, String role) throws CustomException {
		StringBuffer buf = new StringBuffer();
		buf.append(update ? "更新项目成员: ":"添加项目成员: ");
		buf.append('\n');
		buf.append("成员: ").append(memberName).append('\n');
		buf.append("预期回报: ").append(preAmount).append('\n');
		buf.append("角色: ").append(role);
		
		projectRecordService.addRecord(projectId, buf.toString());
	}
	
	/**
	 * 
	 * @param projectId
	 * @param memberId
	 * @return
	 */
	private ProjectMember findByTransactionId(Long transactionId) {
		if(transactionId == null) {
			return null;
		}
		ProjectMember probe = new ProjectMember();
		
		probe.setTransactionId(transactionId);
		probe.setIsDelete(false);
		
		List<ProjectMember> results = this.repository.findAll(Example.of(probe));
		
		if(null == results || results.size() == 0) {
			return null;
		}
		
		return results.get(0);
	}

	private ProjectMember getCurr(Long projectId) throws CustomException {
		Long currUserId = this.getCurrentUser().getId();
		
		MemberVO member = memberService.findByUserId(currUserId);
		
		if(null == currUserId || currUserId.longValue() != member.getUserId()) {
			throw new CustomException("您无权进行此操作");
		}
		
		ProjectMember pm = this.findOne(projectId, member.getId());
		if(null == pm) {
			throw new CustomException("您不是本项目的成员");
		}
		return pm;
	}
}
