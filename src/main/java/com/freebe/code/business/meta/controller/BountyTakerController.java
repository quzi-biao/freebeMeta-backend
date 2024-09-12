package com.freebe.code.business.meta.controller;

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

import com.freebe.code.business.meta.controller.param.BountyDoneParam;
import com.freebe.code.business.meta.controller.param.BountyGiveoutParam;
import com.freebe.code.business.meta.controller.param.BountyTakerParam;
import com.freebe.code.business.meta.controller.param.BountyTakerQueryParam;
import com.freebe.code.business.meta.service.BountyTakerService;
import com.freebe.code.business.meta.vo.BountyTakerVO;
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
@RequestMapping("/bountytaker")
@CrossOrigin(origins = "*")
@Api(tags="悬赏认领接口")
public class BountyTakerController {
	@Autowired
	private BountyTakerService bountytakerService;

	@ApiOperation(value = "创建或者更新悬赏认领")
	@PostMapping("createOrUpdate")
	public ResultBean<BountyTakerVO> createOrUpdate(@Valid @RequestBody BountyTakerParam param) throws CustomException {
		return ResultBean.ok(bountytakerService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取悬赏认领")
	@GetMapping("get/{id}")
	public ResultBean<BountyTakerVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(bountytakerService.findById(id));
	}

	@ApiOperation(value = "查询悬赏认领")
	@PostMapping("list")
	public ResultBean<Page<BountyTakerVO>> list(@Valid @RequestBody BountyTakerQueryParam param) throws CustomException {
		return ResultBean.ok(bountytakerService.queryPage(param));
	}
	
	@ApiOperation(value = "放弃悬赏认领")
	@PostMapping("giveout")
	public ResultBean<BountyTakerVO> list(@Valid @RequestBody BountyGiveoutParam param) throws CustomException {
		return ResultBean.ok(bountytakerService.giveOut(param));
	}
	
	@ApiOperation(value = "完成悬赏")
	@PostMapping("done")
	public ResultBean<BountyTakerVO> submitDone(@Valid @RequestBody BountyDoneParam param) throws CustomException {
		return ResultBean.ok(bountytakerService.doneBounty(param));
	}
}
