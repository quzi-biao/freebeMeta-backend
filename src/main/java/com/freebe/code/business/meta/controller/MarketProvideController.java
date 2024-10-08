package com.freebe.code.business.meta.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.business.meta.service.MarketProvideService;
import com.freebe.code.business.meta.vo.MarketProvideVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.MarkerProvideParam;
import com.freebe.code.business.meta.controller.param.MarkerProvideQueryParam;
import com.freebe.code.business.meta.controller.param.MarketProvideApplyParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/provider")
@CrossOrigin(origins = "*")
@Api(tags="市场供应方接口")
public class MarketProvideController {
	@Autowired
	private MarketProvideService markerproviderService;

	@ApiOperation(value = "创建或者更新市场供应方")
	@PostMapping("createOrUpdate")
	public ResultBean<MarketProvideVO> createOrUpdate(@Valid @RequestBody MarkerProvideParam param) throws CustomException {
		return ResultBean.ok(markerproviderService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取市场供应方")
	@GetMapping("get/{id}")
	public ResultBean<MarketProvideVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(markerproviderService.findById(id));
	}
	
	@ApiOperation(value = "审核市场供应方")
	@GetMapping("audit/{id}")
	public ResultBean<MarketProvideVO> audit(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(markerproviderService.auditProvider(id));
	}
	
	@ApiOperation(value = "成为供应者")
	@PostMapping("apply")
	public ResultBean<MarketProvideVO> applyProvider(@Valid @RequestBody MarketProvideApplyParam param) throws CustomException {
		return ResultBean.ok(markerproviderService.applyToProvider(param));
	}
	
	@ApiOperation(value = "放弃成为供应者")
	@GetMapping("giveout/{id}")
	public ResultBean<MarketProvideVO> giveoutProvider(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(markerproviderService.giveoutProvider(id));
	}

	@ApiOperation(value = "查询市场供应方")
	@PostMapping("list")
	public ResultBean<Page<MarketProvideVO>> list(@Valid @RequestBody MarkerProvideQueryParam param) throws CustomException {
		return ResultBean.ok(markerproviderService.queryPage(param));
	}

	@ApiOperation(value = "删除市场供应方")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		markerproviderService.softDelete(id);
		return ResultBean.ok();
	}

}
