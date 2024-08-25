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

import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.TaskDoneParam;
import com.freebe.code.business.meta.controller.param.TaskTakerParam;
import com.freebe.code.business.meta.controller.param.TaskTakerQueryParam;
import com.freebe.code.business.meta.entity.Task;
import com.freebe.code.business.meta.entity.TaskTaker;
import com.freebe.code.business.meta.repository.TaskTakerRepository;
import com.freebe.code.business.meta.service.TaskService;
import com.freebe.code.business.meta.service.TaskTakerService;
import com.freebe.code.business.meta.type.TaskState;
import com.freebe.code.business.meta.type.TaskTakerState;
import com.freebe.code.business.meta.vo.TaskTakerVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.DateUtils;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class TaskTakerServiceImpl extends BaseServiceImpl<TaskTaker> implements TaskTakerService {
	@Autowired
	private TaskTakerRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;

	@Override
	public TaskTakerVO findById(Long id) throws CustomException {
		TaskTakerVO ret = this.objectCaches.get(id, TaskTakerVO.class);
		if(null == ret){
			Optional<TaskTaker> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public TaskTakerVO createOrUpdate(TaskTakerParam param) throws CustomException {
		if(null == param.getTaskId()) {
			throw new CustomException("参数错误");
		}
		
		Task task = this.taskService.getReference(param.getTaskId());
		if(null == task) {
			throw new CustomException("任务不存在");
		}
		if(task.getState() != TaskState.WAIT_TAKER) {
			throw new CustomException("任务不可认领");
		}
		
		long waitTime = System.currentTimeMillis() - task.getCreateTime();
		if(waitTime > task.getTakerWaitTime() * DateUtils.DAY_PERIOD) {
			task.setState(TaskState.WAIT_TIMEOUT_FAILED);
			this.taskService.update(task.getId(), task);
			throw new CustomException("任务已超时失败");
		}
		
		TaskTaker e = this.getUpdateEntity(param, false);

		e.setTaskId(param.getTaskId());
		e.setTakeTime(System.currentTimeMillis());
		e.setTaker(getCurrentUser().getId());
		e.setState(TaskTakerState.NORMAL);

		e = repository.save(e);
		this.taskService.updateTake(e.getTaskId(), e.getId());
		TaskTakerVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}
	
	@Override
	public TaskTakerVO doneTask(TaskDoneParam param) throws CustomException {
		if(null == param.getTakeId()) {
			throw new CustomException("参数错误");
		}
		
		TaskTaker take = this.getById(param.getTakeId());
		Task task = this.taskService.getReference(take.getTaskId());
		if(take.getState() != TaskTakerState.NORMAL) {
			throw new CustomException("任务状态异常");
		}
		
		if(!checkTaskTimeLimie(take, task)) {
			take.setState(TaskTakerState.TIMEOUT_CANCEL);
			take.setDoneTime(System.currentTimeMillis());
			this.repository.save(take);
			throw new CustomException("任务已超时");
		}
		
		if(task.getState() != TaskState.RUNNING && task.getState() != TaskState.WAIT_AUDIT) {
			throw new CustomException("任务状态异常");
		}
		
		if(task.getTakerId().longValue() != getCurrentUser().getId()) {
			throw new CustomException("您不是任务认领者");
		}
		
		take.setSubmitPictures(toStr(param.getSubmitPictures()));
		take.setSubmitFiles(toStr(param.getSubmitFiles()));
		take.setSubmitDescription(param.getSubmitDescription());
		take.setDoneTime(System.currentTimeMillis());
		take.setState(TaskTakerState.DONE);
		take.setCostTime(take.getDoneTime() - take.getTakeTime());
		
		take = this.repository.save(take);
		
		this.taskService.doneTask(task.getId());
		TaskTakerVO vo = toVO(take);
		objectCaches.put(vo.getId(), vo);
		return vo;
	}

	private boolean checkTaskTimeLimie(TaskTaker take, Task task) {
		if(task.getState() == TaskState.TIMEOUT_FAILED) {
			return false;
		}
		
		long costTime = System.currentTimeMillis() - take.getTakeTime();
		if(costTime > task.getLimitTime() * DateUtils.DAY_PERIOD) {
			task.setState(TaskState.TIMEOUT_FAILED);
			this.taskService.update(task.getId(), task);
			return false;
		}
		
		return true;
	}

	@Override
	public Page<TaskTakerVO> queryPage(TaskTakerQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<TaskTaker> example = buildSpec(param);

		Page<TaskTaker> page = repository.findAll(example, request);
		List<TaskTakerVO> retList = new ArrayList<>();

		for(TaskTaker e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<TaskTakerVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<TaskTaker> buildSpec(TaskTakerQueryParam param) throws CustomException {
		return new Specification<TaskTaker>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<TaskTaker> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<TaskTaker> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("state", param.getState());
				builder.addEqual("taker", param.getTaker());
				builder.addEqual("taskId", param.getTaskId());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private TaskTakerVO toVO(TaskTaker e) throws CustomException {
		TaskTakerVO vo = new TaskTakerVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setTaskId(e.getTaskId());
		vo.setTakeTime(e.getTakeTime());
		vo.setTaker(userService.getUser(e.getTaker()));
		
		vo.setDoneTime(e.getDoneTime());
		vo.setState(e.getState());
		vo.setEvaluate(e.getEvaluate());
		vo.setFreeBe(e.getFreeBe());
		vo.setTransactionId(e.getTransactionId());
		vo.setSubmitFiles(toList(e.getSubmitFiles()));
		vo.setSubmitPictures(toList(e.getSubmitPictures()));
		vo.setSubmitDescription(e.getSubmitDescription());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, TaskTakerVO.class);
		super.softDelete(id);
	}

}
