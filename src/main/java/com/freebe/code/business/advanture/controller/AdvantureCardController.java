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

import com.freebe.code.business.advanture.controller.param.AdvantureCardQueryParam;
import com.freebe.code.business.advanture.service.AdvantureCardService;
import com.freebe.code.business.advanture.vo.AdvantureCardVO;
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
@RequestMapping("/advanturecard")
@CrossOrigin(origins = "*")
@Api(tags="冒险卡片接口")
public class AdvantureCardController {
	@Autowired
	private AdvantureCardService advanturecardService;


	@ApiOperation(value = "获取冒险卡片")
	@GetMapping("get/{id}/{typeId}")
	public ResultBean<AdvantureCardVO> get(@PathVariable("id") Long id, @PathVariable("typeId") Long typeId) throws CustomException {
		return ResultBean.ok(advanturecardService.findByUserId(id, typeId));
	}

	@ApiOperation(value = "查询冒险卡片")
	@PostMapping("list")
	public ResultBean<Page<AdvantureCardVO>> list(@Valid @RequestBody AdvantureCardQueryParam param) throws CustomException {
		return ResultBean.ok(advanturecardService.queryPage(param));
	}


}
