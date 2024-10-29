package com.freebe.code.business.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.business.meta.service.ContentDataService;
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
@RequestMapping("/contentdata")
@CrossOrigin(origins = "*")
@Api(tags="内容数据，做为各个对象富文本等内容的存储接口")
public class ContentDataController {
	@Autowired
	private ContentDataService contentdataService;

	@ApiOperation(value = "获取内容数据，做为各个对象富文本等内容的存储")
	@GetMapping("get/{key}")
	public ResultBean<String> get(@PathVariable("key") String key) throws CustomException {
		return ResultBean.ok("", contentdataService.findByKey(key));
	}

}
