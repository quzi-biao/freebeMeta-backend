package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.business.graph.FreeNode;
import com.freebe.code.business.graph.FreeRelation;
import com.freebe.code.business.meta.controller.param.AuditParam;
import com.freebe.code.business.meta.controller.param.BountyParam;
import com.freebe.code.business.meta.controller.param.BountyQueryParam;
import com.freebe.code.business.meta.controller.param.TransactionParam;
import com.freebe.code.business.meta.entity.Bounty;
import com.freebe.code.business.meta.entity.BountyAuditor;
import com.freebe.code.business.meta.entity.BountyTaker;
import com.freebe.code.business.meta.repository.BountyAuditorRepository;
import com.freebe.code.business.meta.repository.BountyRepository;
import com.freebe.code.business.meta.service.BountyService;
import com.freebe.code.business.meta.service.BountyTakerService;
import com.freebe.code.business.meta.service.ProjectService;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.service.WalletService;
import com.freebe.code.business.meta.service.impl.lucene.BountyLuceneSearch;
import com.freebe.code.business.meta.type.BountyCreateLimit;
import com.freebe.code.business.meta.type.BountyState;
import com.freebe.code.business.meta.type.BountyTakerState;
import com.freebe.code.business.meta.type.Currency;
import com.freebe.code.business.meta.type.MessageType;
import com.freebe.code.business.meta.type.TransactionType;
import com.freebe.code.business.meta.vo.BountyBaseVO;
import com.freebe.code.business.meta.vo.BountyGraph;
import com.freebe.code.business.meta.vo.BountyVO;
import com.freebe.code.business.meta.vo.ProjectMemberVO;
import com.freebe.code.business.meta.vo.ProjectVO;
import com.freebe.code.business.meta.vo.TransactionVO;
import com.freebe.code.business.meta.vo.WalletVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.S;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class BountyServiceImpl extends BaseServiceImpl<Bounty> implements BountyService {
	@Autowired
	private BountyRepository repository;
	
	@Autowired
	private BountyAuditorRepository auditorRepository;

	@Autowired
	private ObjectCaches objectCaches;

	@Autowired
	private BountyTakerService bountyTakerService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private TransactionService transactionService; 
	
	@Autowired
	private ProjectService projectService;

	@Autowired
	private BountyLuceneSearch searcher;

	@Override
	public BountyVO findById(Long id) throws CustomException {
		Bounty ret = findEntityById(id);
		return toVO(ret);
	}

	@Override
	public List<BountyBaseVO> getBaseInfo(List<Long> ids) throws CustomException {
		if(null == ids || ids.size() == 0) {
			return null;
		}
		
		List<BountyBaseVO> ret = new ArrayList<>();
		for(Long id : ids) {
			Bounty b = this.findEntityById(id);
			if(null == b) {
				continue;
			}
			BountyBaseVO base = new BountyBaseVO();
			base.setId(b.getId());
			base.setTitle(b.getTitle());
			base.setState(b.getState());
			
			ret.add(base);
		}
		
		return ret;
	}

	@Transactional
	@Override
	public BountyVO createOrUpdate(BountyParam param) throws CustomException {
		checkParam(param);
		
		Bounty e = this.getUpdateEntity(param, false);
		
		Long userId = getCurrentUser().getId();
		
		e.setProjectId(param.getProjectId());
		e.setOwnerId(userId);
		e.setTitle(param.getTitle());
		e.setDescription(param.getDescription());
		e.setLimitTime(param.getLimitTime());
		e.setTakerWaitTime(param.getTakerWaitTime());
		e.setPriority(param.getPriority());
		if(null == param.getAuditors() || param.getAuditors().size() == 0) {
			param.setAuditors(Arrays.asList(e.getOwnerId()));
		}
		
		if(param.getAuditors().size() > 3) {
			throw new CustomException("审核人数不能超过 3 个");
		}
		
		String auditorStr = auditorsIdToStr(param.getAuditors());
		if(!auditorStr.equals(e.getAuditors())) {
			e.setAuditors(auditorStr);
		}
		
		if(param.getId() == null || e.getState() == BountyState.WAIT_TAKER) {
			e.setReward(param.getReward());
			e.setUreward(currencyStr(param.getUreward()));
			e.setAuditReward(param.getAuditReward());
			e.setState(BountyState.WAIT_TAKER);
		}
		
		List<Long> fronts = param.getFronts();
		List<Long> existFronts = toList(e.getFrontBounties(), Long.class);
		
		e.setFrontBounties(toStr(fronts));
		
		e = repository.save(e);
		
		if(null != existFronts && existFronts.size() > 0) {
			updateNextBounty(e.getId(), fronts, existFronts);
		}else {
			addNextBounty(e.getId(), fronts);
		}
		
		updateAuditor(e);
		
		objectCaches.put(e.getId(), e);
		
		BountyVO vo = toVO(e);
		searcher.addOrUpdateIndex(vo);

		return vo;
	}

	@Transactional
	@Override
	public BountyVO auditBounty(AuditParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getEvaluate())) {
			throw new CustomException("请输入任务评价");
		}
		if(null == param.getPass()) {
			throw new CustomException("参数错误");
		}
		
		Bounty e = this.getById(param.getId());
		if(null == e) {
			throw new CustomException("任务不存在");
		}
		
		if(e.getState() != BountyState.WAIT_AUDIT) {
			throw new CustomException("任务状态异常");
		}
		
		List<Long> auditors = strToAuditorIds(e.getAuditors());
		if(null == auditors || auditors.size() == 0) {
			if(e.getOwnerId().longValue() != getCurrentUser().getId().longValue()) {
				throw new CustomException("您无权执行此操作");
			}
		}else {
			if(auditors.indexOf(getCurrentUser().getId()) < 0) {
				throw new CustomException("您无权进行审核");
			}
		}
		
		BountyTaker tt = this.bountyTakerService.getReference(e.getTakeId());
		if(null == tt) {
			throw new CustomException("认领不存在");
		}
		
		if(tt.getState() != BountyTakerState.DONE) {
			throw new CustomException("认领未完成或已超时");
		}
		
		tt.setEvaluate(param.getEvaluate());
		e.setAuditTime(System.currentTimeMillis());
		e.setAuditorId(getCurrentUser().getId());
		if(param.getPass()) {
			Long srcUserId = e.getOwnerId(); // 从Meta 账户发出
			if(e.getProjectId() != null && e.getProjectId().longValue() > 0) {
				srcUserId = 131L;//this.projectService.getOwnerId(e.getProjectId()); // 从Meta 账户发出
			}
			e.setState(BountyState.DONE);
			Long taker = e.getTakerId();
			if(taker.longValue() != tt.getTaker().longValue()) {
				throw new CustomException("系统混乱");
			}
			
			// 积分回报发放
			Long transactionId = createTransaction(e, srcUserId);
			Long auditTransactionId = createAuditTransaction(e, srcUserId);
			tt.setEvaluate(param.getEvaluate());
			tt.setTransactionId(transactionId);
			e.setAuditTransactionId(auditTransactionId);
			
			e = this.repository.save(e);
			this.bountyTakerService.save(tt);
			// 贡献分发放(贡献分等于积分)
			this.userService.addContribution(taker, e.getReward());
			this.sendMessage(e.getOwnerId(), e.getTakeId(), S.c("您的悬赏已审核通过: [", e.getId(), "]", e.getTitle()), MessageType.BOUNTY_AUDIT_RESULT_MSG);
		}else {
			tt.setState(BountyTakerState.AUDIT_FAILED);
			e.setState(BountyState.AUDIT_FAILED);
			e = this.repository.save(e);
			this.bountyTakerService.save(tt);
			this.sendMessage(e.getOwnerId(), e.getTakeId(), S.c("您的悬赏审核未通过: [", e.getId(), "]", e.getTitle(), ":", param.getEvaluate()), MessageType.BOUNTY_AUDIT_RESULT_MSG);
		}
		
		objectCaches.put(e.getId(), e);
		
		BountyVO vo = toVO(e);
		return vo;
	}
	
	@Transactional
	@Override
	public void restartBounty(Long id) throws CustomException {
		Bounty e = this.getById(id);
		if(null == e) {
			throw new CustomException("任务不存在");
		}
		
//		if(e.getOwnerId().longValue() != this.getCurrentUser().getId().longValue()) {
//			throw new CustomException("您没有权限");
//		}
		
		e.setState(BountyState.WAIT_TAKER);
		e.setTakeId(null);
		e.setTakerId(null);
		
		e = repository.save(e);

		objectCaches.put(e.getId(), e);
	}
	
	@Transactional
	@Override
	public BountyVO cancelBounty(Long bountyId) throws CustomException {
		Bounty e = this.getById(bountyId);
		if(null == e) {
			throw new CustomException("任务不存在");
		}

		if(e.getTakeId() != null) {
			throw new CustomException("任务已被认领，不可取消");
		}
		
		if(e.getOwnerId().longValue() != this.getCurrentUser().getId().longValue()) {
			throw new CustomException("您没有权限");
		}
		
		e.setState(BountyState.CANCEL);
		e = repository.save(e);
		objectCaches.put(e.getId(), e);
		
		BountyVO vo = toVO(e);

		return vo;
	}
	
	@Transactional
	@Override
	public void updateTake(Long bountyId, Long takeId) throws CustomException {
		Bounty bounty = this.getById(bountyId);
		
		if(null == bounty) {
			return;
		}
		
		checkFrontIsDone(bounty);
		
		bounty.setTakerId(getCurrentUser().getId());
		bounty.setTakeId(takeId);
		bounty.setState(BountyState.RUNNING);
		bounty = this.repository.save(bounty);
		
		objectCaches.put(bounty.getId(), bounty);
	}
	
	@Transactional
	@Override
	public void doneBounty(Long bountyId) throws CustomException {
		Bounty bounty = this.getById(bountyId);
		
		if(null == bounty) {
			return;
		}
		
		checkFrontIsDone(bounty);
		
		bounty.setState(BountyState.WAIT_AUDIT);
		bounty.setAuditStartTime(System.currentTimeMillis());
		
		bounty = this.repository.save(bounty);
		objectCaches.put(bounty.getId(), bounty);
		
		List<Long> auditors = strToAuditorIds(bounty.getAuditors());
		if(null == auditors || auditors.size() == 0) {
			auditors = new ArrayList<>();
			auditors.add(bounty.getOwnerId());
		}
		
		this.sendMessage(bounty.getTakerId(), auditors, S.c("[", getCurrentUser().getName() ,"]完成了您的悬赏[", bounty.getId(), "]", bounty.getTitle(), "已完成，请及时审核"), MessageType.BOUNTY_AUDIT_MSG);
	}


	@Override
	public Page<BountyVO> queryPage(BountyQueryParam param) throws CustomException {
		param.setOrder("id");
		List<Order> orders = null;
		if(param.getDesc() == null || param.getDesc()) {
			orders = Arrays.asList(new Order[] {Order.asc("state"), Order.desc(param.getOrder())});
		}else {
			orders = Arrays.asList(new Order[] {Order.asc("state"), Order.asc(param.getOrder())});
		}
		
		PageRequest request = PageRequest.of((int)param.getCurrPage(), (int)param.getLimit(), Sort.by(orders));

		if(null != param.getKeyWords() && !param.getKeyWords().isEmpty()){
			Page<BountyVO> searchPage = searcher.fullTextSearch(param);
			List<Long> idList = new ArrayList<>();
			for(BountyVO e:  searchPage.getContent()) {
				idList.add(e.getId());
			}
			if(idList.isEmpty()) {
				idList.add(-1L);
			}
			param.setIdList(idList);
		}

		Specification<Bounty> example = buildSpec(param);

		Page<Bounty> page = repository.findAll(example, request);
		List<BountyVO> retList = new ArrayList<>();

		for(Bounty e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<BountyVO>(retList, page.getPageable(), page.getTotalElements());
	}
	
	@Override
	public Double countCost(Long projectId) {
		return this.repository.getProjectReward(projectId).doubleValue();
	}
	
	private Long createTransaction(Bounty e, Long srcUserId) throws CustomException {
		// 创建交易
		TransactionParam param = new TransactionParam();
		if(e.getAuditReward() == null) {
			e.setAuditReward(0);
		}
		
		ProjectVO project = this.projectService.findById(e.getProjectId());
		
		param.setAmount(e.getReward().doubleValue() * (100 - e.getAuditReward()) / 100);
		if(null == project.getCurrency()) {
			param.setCurrency(project.getCurrency());
		}else {
			param.setCurrency(Currency.FREE_BE);
		}
		
		param.setSrcWalletId(this.walletService.findByUser(srcUserId).getId());
		
		WalletVO wallet = this.walletService.findByUser(e.getTakerId());
		param.setDstWalletId(wallet.getId());
		param.setMark("完成任务:#" + e.getId() + ", " + e.getTitle());
		if(null == param.getCurrency()) {
			param.setCurrency(Currency.FREE_BE);
		}
		param.setTransactionType(TransactionType.TASK_REWARD);
		param.setProjectId(e.getProjectId());
		
		TransactionVO transaction = this.transactionService.innerCreateOrUpdate(param);
		return transaction.getId();
	}
	
	private Long createAuditTransaction(Bounty e, Long srcUserId) throws CustomException {
		if(e.getAuditorId() == null || e.getAuditReward() == null || e.getAuditReward() == 0) {
			return null;
		}
		// 创建交易
		TransactionParam param = new TransactionParam();
		param.setAmount(e.getReward().doubleValue() * e.getAuditReward() / 100);
		
		ProjectVO project = this.projectService.findById(e.getProjectId());
		if(null == project.getCurrency()) {
			param.setCurrency(Currency.FREE_BE);
		}else {
			param.setCurrency(project.getCurrency());
		}
		
		param.setSrcWalletId(this.walletService.findByUser(srcUserId).getId());
		
		WalletVO wallet = this.walletService.findByUser(e.getAuditorId());
		param.setDstWalletId(wallet.getId());
		param.setMark("任务审核:#" + e.getId() + ", " + e.getTitle());
		if(null == param.getCurrency()) {
			param.setCurrency(Currency.FREE_BE);
		}
		param.setTransactionType(TransactionType.TASK_REWARD);
		param.setProjectId(e.getProjectId());
		
		TransactionVO transaction = this.transactionService.innerCreateOrUpdate(param);
		return transaction.getId();
	}

	private Specification<Bounty> buildSpec(BountyQueryParam param) throws CustomException {
		return new Specification<Bounty>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Bounty> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Bounty> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("projectId", param.getProjectId());
				builder.addEqual("ownerId", param.getOwnerId());
				builder.addIn("state", param.getState());
				builder.addEqual("takerId", param.getTakerId());
				builder.addIn("id", param.getIdList());
				if(null != param.getAuditorId()) {
					builder.addLike("auditors", AUDITOR_ID_TAG + param.getAuditorId());
				}

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private BountyVO toVO(Bounty e) throws CustomException {
		BountyVO vo = new BountyVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setProjectId(e.getProjectId());
		if(null != vo.getProjectId() && vo.getProjectId() != 0) {
			ProjectVO project = this.projectService.findById(vo.getProjectId());
			vo.setProjectName(project.getName());
			vo.setCurrency(project.getCurrency());
		}else {
			vo.setProjectName("个人发布");
			vo.setCurrency(0);
		}
		vo.setOwnerId(e.getOwnerId());

		if(null != e.getOwnerId()) {
			vo.setOwner(userService.getUser(e.getOwnerId()));
		}
		
		if(null != e.getAuditors()) {
			List<Long> auditorIds = strToAuditorIds(e.getAuditors());
			List<UserVO> auditors = new ArrayList<>();
			for(Long auditorId : auditorIds) {
				auditors.add(this.userService.getUser(auditorId));
			}
			vo.setAuditors(auditors);
		}else {
			vo.setAuditors(Arrays.asList(vo.getOwner()));
		}
		
		//vo.setAuditor(this.userService.getUser());
		vo.setTitle(e.getTitle());
		vo.setDescription(e.getDescription());
		vo.setState(e.getState());
		vo.setLimitTime(e.getLimitTime());
		vo.setPriority(e.getPriority());
		
		if(null != e.getTakeId()) {
			vo.setTake(bountyTakerService.findById(e.getTakeId()));
		}
		
		if(e.getAuditorId() != null) {
			vo.setAuditor(this.userService.getUser(e.getAuditorId()));
		}else {
			if(e.getState() == BountyState.AUDIT_FAILED || e.getState() == BountyState.DONE) {
				vo.setAuditor(vo.getOwner());
			}
		}
		
		if(e.getAuditReward() == null) {
			vo.setAuditReward(0);
		}else {
			vo.setAuditReward(e.getAuditReward());
		}
		
		vo.setTakerWaitTime(e.getTakerWaitTime());
		vo.setAuditStartTime(e.getAuditStartTime());
		vo.setAuditTime(e.getAuditTime());
		vo.setReward(e.getReward());
		vo.setUreward(numbericCurrency(e.getUreward()));
		
		vo.setFrontBounties(this.getBaseInfo(toList(e.getFrontBounties(), Long.class)));
		vo.setNextBounties(this.getBaseInfo(toList(e.getNextBounties(), Long.class)));
		
		vo.setCanTake(canTake(vo));
		
		return vo;
	}

	@Override
	public BountyGraph getBountyGraph(Long projectId) throws CustomException {
		Bounty e = new Bounty();
		e.setProjectId(projectId);
		e.setIsDelete(false);
		
		List<Bounty> bounties = this.repository.findAll(Example.of(e));
		if(null == bounties || bounties.size() == 0) {
			return null;
		}
		List<BountyVO> vos = new ArrayList<>();
		for(Bounty b : bounties) {
			vos.add(toVO(b));
		}
		
		BountyGraph g = new BountyGraph();
		g.setNodes(new ArrayList<>());
		g.setRelations(new ArrayList<>());
		
		for(BountyVO vo : vos) {
			if(null == g.getProjectId()) {
				g.setProjectId(vo.getProjectId());
				g.setProjectName(vo.getProjectName());
			}
			
			FreeNode<BountyVO, Long> node = new FreeNode<>();
			node.setNode(vo);
			node.setId(vo.getId());
			
			g.getNodes().add(node);
			
			if(null == vo.getNextBounties() || vo.getNextBounties().size() == 0) {
				continue;
			}
			
			for(BountyBaseVO base : vo.getNextBounties()) {
				FreeRelation<Long> relation = new FreeRelation<>();
				relation.setSrcNode(vo.getId());
				relation.setDstNode(base.getId());
				g.getRelations().add(relation);
			}
		}
		
		return g;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		searcher.deleteIndex(this.findById(id));
		objectCaches.delete(id, BountyVO.class);
		super.softDelete(id);
	}
	
	/**
	 * 更新审核专员信息，过去不存在的新增，过去存在，现在不存在的移除
	 * @param e
	 * @param auditors
	 */
	private void updateAuditor(Bounty e) {
		List<Long> auditors = strToAuditorIds(e.getAuditors());
		if(null == auditors || auditors.size() == 0) {
			return;
		}
		
		BountyAuditor probe = new BountyAuditor();
		probe.setBountyId(e.getId());
		List<BountyAuditor> frontRelations = this.auditorRepository.findAll(Example.of(probe));
		if(null == frontRelations || frontRelations.size() == 0) {
			addAuditorRelation(e.getId(), auditors);
		}
		
		for(Long auditor : auditors) {
			boolean isNew = true;
			for(BountyAuditor front : frontRelations) {
				if(front.getUserId().longValue() == auditor.longValue()) {
					isNew = false;
					break;
				}
			}
			if(isNew) {
				BountyAuditor ba = new BountyAuditor();
				ba.setBountyId(e.getId());
				ba.setUserId(auditor);
				this.auditorRepository.save(ba);
			}
		}
		
		for(BountyAuditor front : frontRelations) {
			boolean remove = true;
			for(Long auditor : auditors) {
				if(front.getUserId().longValue() == auditor.longValue()) {
					remove = false;
					break;
				}
			}
			if(remove) {
				this.auditorRepository.delete(front);
			}
		}
	}
	
	/**
	 * 添加审核关系
	 * @param id
	 * @param auditors
	 */
	private void addAuditorRelation(Long id, List<Long> auditors) {
		for(Long auditor : auditors) {
			BountyAuditor ba = new BountyAuditor();
			ba.setBountyId(id);
			ba.setUserId(auditor);
			
			this.auditorRepository.save(ba);
		}
	}

	/**
	 * 任务是否可领取，后续扩张成任务领取者的筛选机制
	 * @param vo
	 * @return
	 */
	private Boolean canTake(BountyVO vo) {
		if(null == vo.getFrontBounties() || vo.getFrontBounties().size() == 0) {
			return true;
		}
		
		for(BountyBaseVO base : vo.getFrontBounties()) {
			int state = base.getState().intValue();
			if(state != BountyState.DONE && state != BountyState.CANCEL) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查前置悬赏是否完成
	 * @param bounty
	 * @throws CustomException
	 */
	private void checkFrontIsDone(Bounty bounty) throws CustomException {
		List<Long> fronts = toList(bounty.getFrontBounties(), Long.class);
		if(null != fronts && fronts.size() > 0) {
			for(Long front : fronts) {
				Bounty frontBounty = this.findEntityById(front);
				if(null == frontBounty) {
					continue;
				}
				int state = frontBounty.getState();
				if(state != BountyState.DONE && state != BountyState.CANCEL) {
					throw new CustomException("请先完成前置任务");
				}
			}
		}
	}
	
	/**
	 * 比对更新前节点的后置节点
	 * @param id
	 * @param fronts
	 * @param existFronts
	 * @throws CustomException 
	 */
	private void updateNextBounty(Long id, List<Long> fronts, List<Long> existFronts) throws CustomException {
		if(null == fronts) {
			removeNextBounty(id, existFronts);
			return;
		}
		
		// 在之前的，不在现在的，为删除
		List<Long> removed = new ArrayList<>();
		for(Long existFront : existFronts) {
			if(fronts.indexOf(existFront) <= 0) {
				removed.add(existFront);
			}
		}
		if(removed.size() > 0) {
			removeNextBounty(id, removed);
		}
		this.addNextBounty(id, fronts);
	}
	
	/**
	 * 添加后续任务
	 * @param id
	 * @param fronts
	 */
	private void removeNextBounty(Long id, List<Long> fronts) {
		if(null == fronts || fronts.size() == 0) {
			return;
		}
		for(Long front : fronts) {
			Bounty frontBounty = this.findEntityById(front);
			if(null == frontBounty) {
				continue;
			}
			List<Long> nextIds = toList(frontBounty.getNextBounties(), Long.class);
			if(null == nextIds) {
				continue;
			}else {
				if(nextIds.indexOf(id) >= 0) {
					nextIds.remove(id);
					frontBounty.setNextBounties(toStr(nextIds));
					frontBounty = this.repository.save(frontBounty);
					objectCaches.put(frontBounty.getId(), frontBounty);
				}else {
					continue;
				}
			}
		}
	}

	/**
	 * 添加后续任务
	 * @param id
	 * @param fronts
	 * @throws CustomException 
	 */
	private void addNextBounty(Long id, List<Long> fronts) throws CustomException {
		if(null == fronts || fronts.size() == 0) {
			return;
		}
		for(Long front : fronts) {
			Bounty frontBounty = this.findEntityById(front);
			if(null == frontBounty) {
				throw new CustomException("更新失败，前置节点不存在");
			}
			List<Long> nextIds = toList(frontBounty.getNextBounties(), Long.class);
			if(null == nextIds || nextIds.size() == 0) {
				nextIds = new ArrayList<>();
				nextIds.add(id);
			}else {
				if(nextIds.indexOf(id) >= 0) {
					continue;
				}else {
					nextIds.add(id);
				}
			}
			frontBounty.setNextBounties(toStr(nextIds));
			frontBounty = this.repository.save(frontBounty);
			objectCaches.put(frontBounty.getId(), frontBounty);
		}
	}
	
	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	private Bounty findEntityById(Long id) {
		Bounty ret = this.objectCaches.get(id, Bounty.class);
		if(null == ret){
			Optional<Bounty> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
	
	
	private String auditorsIdToStr(List<Long> auditors) {
		return JSONObject.toJSONString(
			auditors.stream().map(item -> AUDITOR_ID_TAG + item).collect(Collectors.toList())
		);
	}
	
	private List<Long> strToAuditorIds(String auditorStr) {
		if(StringUtils.isEmpty(auditorStr)) {
			return null;
		}
		List<String> list = toList(auditorStr, String.class);
		return list.stream().map(item -> Long.parseLong(item.substring(1))).collect(Collectors.toList());
	}
	
	
	private void checkParam(BountyParam param) throws CustomException {
		if(null == param.getProjectId()) {
			throw new CustomException("请设置项目");
		}
		if(StringUtils.isEmpty(param.getTitle())) {
			throw new CustomException("请设置任务名称");
		}
		if(StringUtils.isEmpty(param.getDescription())) {
			throw new CustomException("请填写任务说明");
		}
		if(null == param.getReward() || param.getReward() == 0) {
			throw new CustomException("请设置任务赏金");
		}
		if(null == param.getTakerWaitTime()) {
			throw new CustomException("请设置任务认领等待时间");
		}
		if(param.getTakerWaitTime() <= 0 || param.getTakerWaitTime() > 100) {
			throw new CustomException("任务等待时间不得超过10天");
		}
		if(null == param.getLimitTime()) {
			throw new CustomException("请设置任务完成时间");
		}
		if(param.getLimitTime() <= 0 || param.getLimitTime() > 15) {
			throw new CustomException("任务完成时间不得超过7天，您应该细分您的任务");
		}
		if(param.getAuditReward() == null) {
			param.setAuditReward(0);
		}else {
			if(param.getAuditReward() < 0 || param.getAuditReward() >= 100) {
				throw new CustomException("任务审核奖励为百分比，取值范围为 0-100");
			}
		}
		
		if(param.getProjectId() > 0) {
			ProjectVO project = this.projectService.findById(param.getProjectId());
			if(null == project) {
				throw new CustomException("项目不存在");
			}
			Long userId = getCurrentUser().getId();
			Integer createLimit = project.getBountyCreateLimit();
			if(null == createLimit || createLimit == BountyCreateLimit.OWNER) {
				if(userId.longValue() != project.getOwnerId().longValue()) {
					throw new CustomException("您不是项目所有者");
				}
			}else if(createLimit == BountyCreateLimit.MEMBER) {
				if(!this.isMember(this.getCurrentUser().getId(), project.getMembers())) {
					throw new CustomException("您不是项目成员");
				}
			}
			WalletVO wallet = this.walletService.findByUser(project.getOwnerId());
			if(wallet.getFreeBe() < param.getReward()) {
				throw new CustomException("项目主理人的钱包余额不足");
			}
		}else {
			WalletVO wallet = this.walletService.findByUser(this.getCurrentUser().getId());
			if(wallet.getFreeBe() < param.getReward()) {
				throw new CustomException("您的钱包余额不足");
			}
		}
	}

	private boolean isMember(Long id, List<ProjectMemberVO> members) {
		if(null == members || members.size() == 0) {
			return true;
		}
		
		for(ProjectMemberVO m : members) {
			if(m.getMember().getUser().getId().longValue() == id.longValue()) {
				return true;
			}
		}
		return false;
	}
}
