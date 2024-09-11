package com.freebe.code.business.advanture.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeAuditParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeQueryParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeSubmitParam;
import com.freebe.code.business.advanture.service.AdvantureTaskTakeService;
import com.freebe.code.business.advanture.vo.AdvantureTaskTakeVO;
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
@RequestMapping("/advanturetasktake")
@CrossOrigin(origins = "*")
@Api(tags="任务领取接口")
public class AdvantureTaskTakeController {
	@Autowired
	private AdvantureTaskTakeService advanturetasktakeService;

	@ApiOperation(value = "创建或者更新任务领取")
	@PostMapping("createOrUpdate")
	public ResultBean<AdvantureTaskTakeVO> createOrUpdate(@Valid @RequestBody AdvantureTaskTakeParam param) throws CustomException {
		return ResultBean.ok(advanturetasktakeService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取任务领取")
	@GetMapping("get/{id}")
	public ResultBean<AdvantureTaskTakeVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(advanturetasktakeService.findById(id));
	}

	@ApiOperation(value = "查询任务领取")
	@PostMapping("list")
	public ResultBean<Page<AdvantureTaskTakeVO>> list(@Valid @RequestBody AdvantureTaskTakeQueryParam param) throws CustomException {
		return ResultBean.ok(advanturetasktakeService.queryPage(param));
	}

	@ApiOperation(value = "放弃领取")
	@PostMapping("giveout")
	public ResultBean<AdvantureTaskTakeVO> giveout(@Valid @RequestBody AdvantureTaskTakeSubmitParam param) throws CustomException {
		return ResultBean.ok(advanturetasktakeService.giveOut(param));
	}

	@ApiOperation(value = "提交领取")
	@PostMapping("submit")
	public ResultBean<AdvantureTaskTakeVO> submit(@Valid @RequestBody AdvantureTaskTakeSubmitParam param) throws CustomException {
		return ResultBean.ok(advanturetasktakeService.submit(param));
	}
	
	@ApiOperation(value = "提交领取")
	@PostMapping("audit")
	public ResultBean<AdvantureTaskTakeVO> audit(@Valid @RequestBody AdvantureTaskTakeAuditParam param) throws CustomException {
		return ResultBean.ok(advanturetasktakeService.audit(param));
	}
}
