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

import com.freebe.code.business.meta.controller.param.BountyAuditParam;
import com.freebe.code.business.meta.controller.param.BountyParam;
import com.freebe.code.business.meta.controller.param.BountyQueryParam;
import com.freebe.code.business.meta.service.BountyService;
import com.freebe.code.business.meta.vo.BountyVO;
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
@RequestMapping("/bounty")
@CrossOrigin(origins = "*")
@Api(tags="项目悬赏接口")
public class BountyController {
	@Autowired
	private BountyService bountyService;

	@ApiOperation(value = "创建或者更新项目悬赏")
	@PostMapping("createOrUpdate")
	public ResultBean<BountyVO> createOrUpdate(@Valid @RequestBody BountyParam param) throws CustomException {
		return ResultBean.ok(bountyService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取项目悬赏")
	@GetMapping("get/{id}")
	public ResultBean<BountyVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(bountyService.findById(id));
	}

	@ApiOperation(value = "查询项目悬赏")
	@PostMapping("list")
	public ResultBean<Page<BountyVO>> list(@Valid @RequestBody BountyQueryParam param) throws CustomException {
		return ResultBean.ok(bountyService.queryPage(param));
	}
	
	@ApiOperation(value = "取消目悬赏")
	@GetMapping("cancel/{id}")
	public ResultBean<BountyVO> cancel(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(bountyService.cancelBounty(id));
	}

	@ApiOperation(value = "悬赏审核")
	@PostMapping("audit")
	public ResultBean<BountyVO> auditBounty(@Valid @RequestBody BountyAuditParam param) throws CustomException {
		return ResultBean.ok(bountyService.auditBounty(param));
	}

	@ApiOperation(value = "删除项目悬赏")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		bountyService.softDelete(id);
		return ResultBean.ok();
	}

}
