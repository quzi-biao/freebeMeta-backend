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

import com.freebe.code.business.meta.controller.param.TransactionParam;
import com.freebe.code.business.meta.controller.param.TransactionQueryParam;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.vo.TransactionVO;
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
@RequestMapping("/transaction")
@CrossOrigin(origins = "*")
@Api(tags="交易接口")
public class TransactionController {
	@Autowired
	private TransactionService transactionService;

	@ApiOperation(value = "创建或者更新交易")
	@PostMapping("createOrUpdate")
	public ResultBean<TransactionVO> createOrUpdate(@Valid @RequestBody TransactionParam param) throws CustomException {
		return ResultBean.ok(transactionService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取交易")
	@GetMapping("get/{id}")
	public ResultBean<TransactionVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(transactionService.findById(id));
	}

	@ApiOperation(value = "查询交易")
	@PostMapping("list")
	public ResultBean<Page<TransactionVO>> list(@Valid @RequestBody TransactionQueryParam param) throws CustomException {
		return ResultBean.ok(transactionService.queryPage(param));
	}
	
	@ApiOperation(value = "交易审核通过")
	@GetMapping("audit/{id}")
	public ResultBean<TransactionVO> audit(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(transactionService.transactionAudit(id));
	}
	
	@ApiOperation(value = "撤销交易")
	@GetMapping("reback/{id}")
	public ResultBean<TransactionVO> reback(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(transactionService.reback(id));
	}

}
