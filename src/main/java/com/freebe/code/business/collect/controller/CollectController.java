package com.freebe.code.business.collect.controller;

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

import com.freebe.code.business.collect.controller.param.CollectParam;
import com.freebe.code.business.collect.controller.param.CollectQueryParam;
import com.freebe.code.business.collect.service.CollectService;
import com.freebe.code.business.collect.vo.CollectVO;
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
@RequestMapping("/collect")
@CrossOrigin(origins = "*")
@Api(tags="收藏接口")
public class CollectController {
	@Autowired
	private CollectService collectService;

	@ApiOperation(value = "创建或者更新收藏")
	@PostMapping("createOrUpdate")
	public ResultBean<CollectVO> createOrUpdate(@Valid @RequestBody CollectParam param) throws CustomException {
		return ResultBean.ok(collectService.createOrUpdate(param));
	}
	
	@ApiOperation(value = "取消收藏")
	@PostMapping("cancel")
	public ResultBean<CollectVO> cancel(@Valid @RequestBody CollectParam param) throws CustomException {
		return ResultBean.ok(collectService.cancel(param));
	}

	@ApiOperation(value = "获取收藏")
	@GetMapping("get/{id}")
	public ResultBean<CollectVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(collectService.findById(id));
	}

	@ApiOperation(value = "查询收藏")
	@PostMapping("list")
	public ResultBean<Page<CollectVO>> list(@Valid @RequestBody CollectQueryParam param) throws CustomException {
		return ResultBean.ok(collectService.queryPage(param));
	}

//	@ApiOperation(value = "删除收藏")
//	@DeleteMapping("{id}")
//	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
//		collectService.softDelete(id);
//		return ResultBean.ok();
//	}

}
