package com.freebe.code.business.website.template.controller;

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

import com.freebe.code.business.website.template.controller.param.WebsiteTemplateEntityParam;
import com.freebe.code.business.website.template.controller.param.WebsiteTemplateEntityQueryParam;
import com.freebe.code.business.website.template.service.WebsiteTemplateEntityService;
import com.freebe.code.business.website.template.vo.WebsiteTemplateEntityVO;
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
@RequestMapping("/websitetemplate")
@CrossOrigin(origins = "*")
@Api(tags="网页模版接口")
public class WebsiteTemplateEntityController {
	@Autowired
	private WebsiteTemplateEntityService websitetemplateentityService;

	@ApiOperation(value = "创建或者更新网页模版")
	@PostMapping("createOrUpdate")
	public ResultBean<WebsiteTemplateEntityVO> createOrUpdate(@Valid @RequestBody WebsiteTemplateEntityParam param) throws CustomException {
		return ResultBean.ok(websitetemplateentityService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取网页模版")
	@GetMapping("get/{id}")
	public ResultBean<WebsiteTemplateEntityVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(websitetemplateentityService.findById(id));
	}

	@ApiOperation(value = "查询网页模版")
	@PostMapping("list")
	public ResultBean<Page<WebsiteTemplateEntityVO>> list(@Valid @RequestBody WebsiteTemplateEntityQueryParam param) throws CustomException {
		return ResultBean.ok(websitetemplateentityService.queryPage(param));
	}

	@ApiOperation(value = "删除网页模版")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		websitetemplateentityService.softDelete(id);
		return ResultBean.ok();
	}

}
