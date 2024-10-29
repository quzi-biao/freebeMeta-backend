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

import com.freebe.code.business.meta.controller.param.LikeParam;
import com.freebe.code.business.meta.controller.param.LikeQueryParam;
import com.freebe.code.business.meta.service.LikeService;
import com.freebe.code.business.meta.vo.LikeVO;
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
@RequestMapping("/like")
@CrossOrigin(origins = "*")
@Api(tags="喜爱接口")
public class LikeController {
	@Autowired
	private LikeService likeService;

	@ApiOperation(value = "创建或者更新收藏")
	@PostMapping("createOrUpdate")
	public ResultBean<LikeVO> createOrUpdate(@Valid @RequestBody LikeParam param) throws CustomException {
		return ResultBean.ok(likeService.createOrUpdate(param));
	}
	
	@ApiOperation(value = "取消收藏")
	@PostMapping("cancel")
	public ResultBean<LikeVO> cancel(@Valid @RequestBody LikeParam param) throws CustomException {
		return ResultBean.ok(likeService.cancel(param));
	}

	@ApiOperation(value = "获取收藏")
	@GetMapping("get/{id}")
	public ResultBean<LikeVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(likeService.findById(id));
	}

	@ApiOperation(value = "查询收藏")
	@PostMapping("list")
	public ResultBean<Page<LikeVO>> list(@Valid @RequestBody LikeQueryParam param) throws CustomException {
		return ResultBean.ok(likeService.queryPage(param));
	}

}
