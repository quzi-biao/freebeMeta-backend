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

import com.freebe.code.business.meta.service.MarketProviderService;
import com.freebe.code.business.meta.vo.MarketProviderVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.MarkerProviderParam;
import com.freebe.code.business.meta.controller.param.MarkerProviderQueryParam;

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
public class MarketProviderController {
	@Autowired
	private MarketProviderService markerproviderService;

	@ApiOperation(value = "创建或者更新市场供应方")
	@PostMapping("createOrUpdate")
	public ResultBean<MarketProviderVO> createOrUpdate(@Valid @RequestBody MarkerProviderParam param) throws CustomException {
		return ResultBean.ok(markerproviderService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取市场供应方")
	@GetMapping("get/{id}")
	public ResultBean<MarketProviderVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(markerproviderService.findById(id));
	}
	
	@ApiOperation(value = "审核市场供应方")
	@GetMapping("audit/{id}")
	public ResultBean<MarketProviderVO> audit(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(markerproviderService.auditProvider(id));
	}
	
	@ApiOperation(value = "获取联系方式")
	@GetMapping("contact/{id}")
	public ResultBean<String> getContact(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok("", markerproviderService.getContact(id));
	}

	@ApiOperation(value = "查询市场供应方")
	@PostMapping("list")
	public ResultBean<Page<MarketProviderVO>> list(@Valid @RequestBody MarkerProviderQueryParam param) throws CustomException {
		return ResultBean.ok(markerproviderService.queryPage(param));
	}

	@ApiOperation(value = "删除市场供应方")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		markerproviderService.softDelete(id);
		return ResultBean.ok();
	}

}
