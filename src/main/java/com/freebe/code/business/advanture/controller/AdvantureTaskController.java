package com.freebe.code.business.advanture.controller;

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

import com.freebe.code.business.advanture.service.AdvantureTaskService;
import com.freebe.code.business.advanture.vo.AdvantureTaskVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/advanturetask")
@CrossOrigin(origins = "*")
@Api(tags="冒险卡片接口")
public class AdvantureTaskController {
	@Autowired
	private AdvantureTaskService advanturetaskService;

	@ApiOperation(value = "创建或者更新冒险卡片")
	@PostMapping("createOrUpdate")
	public ResultBean<AdvantureTaskVO> createOrUpdate(@Valid @RequestBody AdvantureTaskParam param) throws CustomException {
		return ResultBean.ok(advanturetaskService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取冒险卡片")
	@GetMapping("get/{id}")
	public ResultBean<AdvantureTaskVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(advanturetaskService.findById(id));
	}

	@ApiOperation(value = "查询冒险卡片")
	@PostMapping("list")
	public ResultBean<Page<AdvantureTaskVO>> list(@Valid @RequestBody AdvantureTaskQueryParam param) throws CustomException {
		return ResultBean.ok(advanturetaskService.queryPage(param));
	}

	@ApiOperation(value = "删除冒险卡片")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		advanturetaskService.softDelete(id);
		return ResultBean.ok();
	}

}
