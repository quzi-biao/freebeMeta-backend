package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.BountyAuditParam;
import com.freebe.code.business.meta.controller.param.BountyParam;
import com.freebe.code.business.meta.controller.param.BountyQueryParam;
import com.freebe.code.business.meta.controller.param.TransactionParam;
import com.freebe.code.business.meta.entity.Bounty;
import com.freebe.code.business.meta.entity.BountyTaker;
import com.freebe.code.business.meta.repository.BountyRepository;
import com.freebe.code.business.meta.service.BountyService;
import com.freebe.code.business.meta.service.BountyTakerService;
import com.freebe.code.business.meta.service.ProjectService;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.service.WalletService;
import com.freebe.code.business.meta.service.impl.lucene.BountyLuceneSearch;
import com.freebe.code.business.meta.type.BountyState;
import com.freebe.code.business.meta.type.BountyTakerState;
import com.freebe.code.business.meta.type.Currency;
import com.freebe.code.business.meta.type.TransactionType;
import com.freebe.code.business.meta.vo.BountyVO;
import com.freebe.code.business.meta.vo.TransactionVO;
import com.freebe.code.business.meta.vo.WalletVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
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
	private ObjectCaches objectCaches;

	@Autowired
	private BountyTakerService taskTakerService;

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
		BountyVO ret = this.objectCaches.get(id, BountyVO.class);
		if(null == ret){
			Optional<Bounty> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public BountyVO createOrUpdate(BountyParam param) throws CustomException {
		checkParam(param);
		
		Bounty e = this.getUpdateEntity(param, false);

		e.setProjectId(param.getProjectId());
		e.setOwnerId(getCurrentUser().getId());
		e.setTitle(param.getTitle());
		e.setDescription(param.getDescription());
		e.setState(BountyState.WAIT_TAKER);
		e.setLimitTime(param.getLimitTime());
		e.setTakerWaitTime(param.getTakerWaitTime());
		if(param.getId() == null) {
			e.setReward(param.getReward());
		}

		e = repository.save(e);

		BountyVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);
		searcher.addOrUpdateIndex(vo);

		return vo;
	}
	
	@Transactional
	@Override
	public BountyVO auditBounty(BountyAuditParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getEvaluate())) {
			throw new CustomException("请输入任务评价");
		}
		if(null == param.getPass()) {
			throw new CustomException("参数错误");
		}
		
		Bounty e = this.getById(param.getBountyId());
		if(null == e) {
			throw new CustomException("任务不存在");
		}
		
		if(e.getState() != BountyState.WAIT_AUDIT) {
			throw new CustomException("任务状态异常");
		}
		
		if(e.getOwnerId().longValue() != getCurrentUser().getId().longValue()) {
			throw new CustomException("您无权执行此操作");
		}
		
		BountyTaker tt = this.taskTakerService.getReference(e.getTakeId());
		if(null == tt) {
			throw new CustomException("认领不存在");
		}
		
		if(tt.getState() != BountyTakerState.DONE) {
			throw new CustomException("认领未完成或已超时");
		}
		
		tt.setEvaluate(param.getEvaluate());
		e.setAuditTime(System.currentTimeMillis());
		if(param.getPass()) {
			e.setState(BountyState.DONE);
			this.repository.save(e);
			Long taker = e.getTakerId();
			if(taker != tt.getTaker()) {
				throw new CustomException("系统混乱");
			}
			
			long transactionId = createTransaction(e);
			tt.setEvaluate(param.getEvaluate());
			tt.setTransactionId(transactionId);
			this.taskTakerService.save(tt);
		}else {
			tt.setState(BountyTakerState.AUDIT_FAILED);
			e.setState(BountyState.AUDIT_FAILED);
			this.repository.save(e);
			this.taskTakerService.save(tt);
		}
		
		BountyVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	private Long createTransaction(Bounty e) throws CustomException {
		// 创建交易
		TransactionParam param = new TransactionParam();
		param.setAmount(e.getReward().doubleValue());
		param.setCurrency(Currency.FREE_BE);
		
		param.setSrcWalletId(this.walletService.findByUser(e.getOwnerId()).getId());
		
		WalletVO wallet = this.walletService.findByUser(e.getTakerId());
		param.setDstWalletId(wallet.getId());
		param.setMark("完成任务:" + e.getTitle());
		if(null == param.getCurrency()) {
			param.setCurrency(Currency.FREE_BE);
		}
		param.setTransactionType(TransactionType.TASK_REWARD);
		param.setProjectId(e.getProjectId());
		
		TransactionVO transaction = this.transactionService.innerCreateOrUpdate(param);
		return transaction.getId();
	}

	@Override
	public BountyVO cancelBounty(Long taskId) throws CustomException {
		Bounty e = this.getById(taskId);
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

		BountyVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}
	

	@Override
	public void updateTake(Long taskId, Long takeId) throws CustomException {
		Bounty task = this.getById(taskId);
		
		if(null == task) {
			return;
		}
		
		task.setTakerId(getCurrentUser().getId());
		task.setTakeId(takeId);
		task.setState(BountyState.RUNNING);
		task = this.repository.save(task);
		
		BountyVO vo = toVO(task);
		objectCaches.put(vo.getId(), vo);
	}
	
	@Override
	public void doneBounty(Long taskId) throws CustomException {
		Bounty task = this.getById(taskId);
		
		if(null == task) {
			return;
		}
		
		task.setState(BountyState.WAIT_AUDIT);
		task.setAuditStartTime(System.currentTimeMillis());
		
		task = this.repository.save(task);
		BountyVO vo = toVO(task);
		objectCaches.put(vo.getId(), vo);
	}

	@Override
	public Page<BountyVO> queryPage(BountyQueryParam param) throws CustomException {
//		if(null != param.getKeyWords() && param.getKeyWords().length() != 0){
//			return queryPageWithKeywords(param);
//		}

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
			vo.setProjectName(this.projectService.findById(vo.getProjectId()).getName());
		}else {
			vo.setProjectName("个人发布");
		}
		vo.setOwnerId(e.getOwnerId());

		if(null != e.getOwnerId()) {
			vo.setOwner(userService.getUser(e.getOwnerId()));
		}
		
		vo.setTitle(e.getTitle());
		vo.setDescription(e.getDescription());
		vo.setState(e.getState());
		vo.setLimitTime(e.getLimitTime());

		if(null != e.getTakeId()) {
			vo.setTake(taskTakerService.findById(e.getTakeId()));
		}
		
		vo.setTakerWaitTime(e.getTakerWaitTime());
		vo.setAuditStartTime(e.getAuditStartTime());
		vo.setReward(e.getReward());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		searcher.deleteIndex(this.findById(id));
		objectCaches.delete(id, BountyVO.class);
		super.softDelete(id);
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
		if(param.getTakerWaitTime() <= 0 || param.getTakerWaitTime() > 10) {
			throw new CustomException("任务等待时间不得超过10天");
		}
		if(null == param.getLimitTime()) {
			throw new CustomException("请设置任务完成时间");
		}
		if(param.getLimitTime() <= 0 || param.getLimitTime() > 7) {
			throw new CustomException("任务完成时间不得超过7天，您应该细分您的任务");
		}
		
		WalletVO wallet = this.walletService.findByUser(this.getCurrentUser().getId());
		if(wallet.getFreeBe() < param.getReward()) {
			throw new CustomException("您的钱包余额不足");
		}
	}

}
