package com.freebe.code.business.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.business.meta.service.WalletService;
import com.freebe.code.business.meta.vo.FinanceInfo;
import com.freebe.code.business.meta.vo.WalletVO;
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
@RequestMapping("/wallet")
@CrossOrigin(origins = "*")
@Api(tags="钱包接口")
public class WalletController {
	@Autowired
	private WalletService walletService;

//	@ApiOperation(value = "创建或者更新钱包")
//	@PostMapping("createOrUpdate")
//	public ResultBean<WalletVO> createOrUpdate(@Valid @RequestBody WalletParam param) throws CustomException {
//		return ResultBean.ok(walletService.createOrUpdate(param));
//	}

	@ApiOperation(value = "获取钱包")
	@GetMapping("get/{id}")
	public ResultBean<WalletVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(walletService.findById(id));
	}
	
	@ApiOperation(value = "获取资产信息")
	@GetMapping("finance")
	public ResultBean<FinanceInfo> finace() throws CustomException {
		return ResultBean.ok(walletService.getFinanceInfo());
	}
//
//	@ApiOperation(value = "查询钱包")
//	@PostMapping("list")
//	public ResultBean<Page<WalletVO>> list(@Valid @RequestBody WalletQueryParam param) throws CustomException {
//		return ResultBean.ok(walletService.queryPage(param));
//	}
//
//	@ApiOperation(value = "删除钱包")
//	@DeleteMapping("{id}")
//	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
//		walletService.softDelete(id);
//		return ResultBean.ok();
//	}

}
