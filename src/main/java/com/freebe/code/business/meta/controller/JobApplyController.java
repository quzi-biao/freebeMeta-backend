package com.freebe.code.business.meta.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.business.meta.controller.param.AuditParam;
import com.freebe.code.business.meta.controller.param.JobApplyParam;
import com.freebe.code.business.meta.controller.param.JobApplyQueryParam;
import com.freebe.code.business.meta.service.JobApplyService;
import com.freebe.code.business.meta.vo.JobApplyVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/jobapply")
@CrossOrigin(origins = "*")
@Api(tags="岗位申请接口")
public class JobApplyController {
	@Autowired
	private JobApplyService jobapplyService;

	@ApiOperation(value = "创建或者更新岗位申请")
	@PostMapping("createOrUpdate")
	public ResultBean<JobApplyVO> createOrUpdate(@Valid @RequestBody JobApplyParam param) throws CustomException {
		return ResultBean.ok(jobapplyService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取岗位申请")
	@GetMapping("get/{id}")
	public ResultBean<JobApplyVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(jobapplyService.findById(id));
	}
	
	@ApiOperation(value = "问卷回答完成")
	@GetMapping("answer/end/{id}")
	public ResultBean<JobApplyVO> answer(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(jobapplyService.questionnaireEnd(id));
	}
	
	@ApiOperation(value = "任务完成")
	@GetMapping("task/end/{id}")
	public ResultBean<JobApplyVO> taskEnd(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(jobapplyService.taskEnd(id));
	}
	
	@ApiOperation(value = "问卷审核")
	@GetMapping("answer/audit")
	public ResultBean<JobApplyVO> answerAudit(@Valid @RequestBody AuditParam param) throws CustomException {
		return ResultBean.ok(jobapplyService.answerAudit(param));
	}
	
	@ApiOperation(value = "问卷审核")
	@GetMapping("task/audit")
	public ResultBean<JobApplyVO> taskAudit(@Valid @RequestBody AuditParam param) throws CustomException {
		return ResultBean.ok(jobapplyService.taskAudit(param));
	}
	
	@ApiOperation(value = "申请审核")
	@GetMapping("review")
	public ResultBean<JobApplyVO> review(@Valid @RequestBody AuditParam param) throws CustomException {
		return ResultBean.ok(jobapplyService.review(param));
	}

	@ApiOperation(value = "查询岗位申请")
	@PostMapping("list")
	public ResultBean<Page<JobApplyVO>> list(@Valid @RequestBody JobApplyQueryParam param) throws CustomException {
		return ResultBean.ok(jobapplyService.queryPage(param));
	}

	@ApiOperation(value = "删除岗位申请")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		jobapplyService.softDelete(id);
		return ResultBean.ok();
	}

}
