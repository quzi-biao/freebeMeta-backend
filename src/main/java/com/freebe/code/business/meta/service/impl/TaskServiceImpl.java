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

import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.TaskAuditParam;
import com.freebe.code.business.meta.controller.param.TaskParam;
import com.freebe.code.business.meta.controller.param.TaskQueryParam;
import com.freebe.code.business.meta.controller.param.TransactionParam;
import com.freebe.code.business.meta.entity.Task;
import com.freebe.code.business.meta.entity.TaskTaker;
import com.freebe.code.business.meta.repository.TaskRepository;
import com.freebe.code.business.meta.service.ProjectService;
import com.freebe.code.business.meta.service.TaskService;
import com.freebe.code.business.meta.service.TaskTakerService;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.service.WalletService;
import com.freebe.code.business.meta.type.Currency;
import com.freebe.code.business.meta.type.TaskState;
import com.freebe.code.business.meta.type.TaskTakerState;
import com.freebe.code.business.meta.type.TransactionType;
import com.freebe.code.business.meta.vo.TaskVO;
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
public class TaskServiceImpl extends BaseServiceImpl<Task> implements TaskService {
	@Autowired
	private TaskRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private TaskTakerService taskTakerService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private TransactionService transactionService; 
	
	@Autowired
	private ProjectService projectService;

	@Override
	public TaskVO findById(Long id) throws CustomException {
		TaskVO ret = this.objectCaches.get(id, TaskVO.class);
		if(null == ret){
			Optional<Task> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public TaskVO createOrUpdate(TaskParam param) throws CustomException {
		checkParam(param);
		
		Task e = this.getUpdateEntity(param, false);

		e.setProjectId(param.getProjectId());
		e.setOwnerId(getCurrentUser().getId());
		e.setTitle(param.getTitle());
		e.setDescription(param.getDescription());
		e.setState(TaskState.WAIT_TAKER);
		e.setLimitTime(param.getLimitTime());
		e.setTakerWaitTime(param.getTakerWaitTime());
		if(param.getId() == null) {
			e.setReward(param.getReward());
		}

		e = repository.save(e);

		TaskVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}
	
	@Transactional
	@Override
	public TaskVO auditTask(TaskAuditParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getEvaluate())) {
			throw new CustomException("请输入任务评价");
		}
		if(null == param.getPass()) {
			throw new CustomException("参数错误");
		}
		
		Task e = this.getById(param.getTaskId());
		if(null == e) {
			throw new CustomException("任务不存在");
		}
		
		if(e.getState() != TaskState.WAIT_AUDIT) {
			throw new CustomException("任务状态异常");
		}
		
		if(e.getOwnerId().longValue() != getCurrentUser().getId().longValue()) {
			throw new CustomException("您无权执行此操作");
		}
		
		TaskTaker tt = this.taskTakerService.getReference(e.getTakeId());
		if(null == tt) {
			throw new CustomException("认领不存在");
		}
		
		if(tt.getState() != TaskTakerState.DONE) {
			throw new CustomException("认领未完成或已超时");
		}
		
		tt.setEvaluate(param.getEvaluate());
		e.setAuditTime(System.currentTimeMillis());
		if(param.getPass()) {
			e.setState(TaskState.DONE);
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
			tt.setState(TaskTakerState.AUDIT_FAILED);
			e.setState(TaskState.AUDIT_FAILED);
			this.repository.save(e);
			this.taskTakerService.save(tt);
		}
		
		TaskVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	private Long createTransaction(Task e) throws CustomException {
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
	public TaskVO cancelTask(Long taskId) throws CustomException {
		Task e = this.getById(taskId);
		if(null == e) {
			throw new CustomException("任务不存在");
		}

		if(e.getTakeId() != null) {
			throw new CustomException("任务已被认领，不可取消");
		}
		
		if(e.getOwnerId().longValue() != this.getCurrentUser().getId().longValue()) {
			throw new CustomException("您没有权限");
		}
		
		e.setState(TaskState.CANCEL);
		e = repository.save(e);

		TaskVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}
	

	@Override
	public void updateTake(Long taskId, Long takeId) throws CustomException {
		Task task = this.getById(taskId);
		
		if(null == task) {
			return;
		}
		
		task.setTakerId(getCurrentUser().getId());
		task.setTakeId(takeId);
		task.setState(TaskState.RUNNING);
		task = this.repository.save(task);
		
		TaskVO vo = toVO(task);
		objectCaches.put(vo.getId(), vo);
	}
	
	@Override
	public void doneTask(Long taskId) throws CustomException {
		Task task = this.getById(taskId);
		
		if(null == task) {
			return;
		}
		
		task.setState(TaskState.WAIT_AUDIT);
		task.setAuditStartTime(System.currentTimeMillis());
		
		task = this.repository.save(task);
		TaskVO vo = toVO(task);
		objectCaches.put(vo.getId(), vo);
	}

	@Override
	public Page<TaskVO> queryPage(TaskQueryParam param) throws CustomException {
		param.setOrder("id");
		List<Order> orders = null;
		if(param.getDesc() == null || param.getDesc()) {
			orders = Arrays.asList(new Order[] {Order.asc("state"), Order.desc(param.getOrder())});
		}else {
			orders = Arrays.asList(new Order[] {Order.asc("state"), Order.asc(param.getOrder())});
		}
		
		PageRequest request = PageRequest.of((int)param.getCurrPage(), (int)param.getLimit(), Sort.by(orders));

		Specification<Task> example = buildSpec(param);

		Page<Task> page = repository.findAll(example, request);
		List<TaskVO> retList = new ArrayList<>();

		for(Task e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<TaskVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<Task> buildSpec(TaskQueryParam param) throws CustomException {
		return new Specification<Task>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Task> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("projectId", param.getProjectId());
				builder.addEqual("ownerId", param.getOwnerId());
				builder.addIn("state", param.getState());
				builder.addEqual("takerId", param.getTakerId());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private TaskVO toVO(Task e) throws CustomException {
		TaskVO vo = new TaskVO();
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
		objectCaches.delete(id, TaskVO.class);
		super.softDelete(id);
	}
	
	
	private void checkParam(TaskParam param) throws CustomException {
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
