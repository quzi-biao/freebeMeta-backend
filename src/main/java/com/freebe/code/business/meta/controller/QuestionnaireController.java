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

import com.freebe.code.business.meta.service.QuestionnaireService;
import com.freebe.code.business.meta.vo.QuestionnaireVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.QuestionnaireParam;
import com.freebe.code.business.meta.controller.param.QuestionnaireQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/questionnaire")
@CrossOrigin(origins = "*")
@Api(tags="问卷接口")
public class QuestionnaireController {
	@Autowired
	private QuestionnaireService questionnaireService;

	@ApiOperation(value = "创建或者更新问卷")
	@PostMapping("createOrUpdate")
	public ResultBean<QuestionnaireVO> createOrUpdate(@Valid @RequestBody QuestionnaireParam param) throws CustomException {
		return ResultBean.ok(questionnaireService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取问卷")
	@GetMapping("get/{id}")
	public ResultBean<QuestionnaireVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(questionnaireService.findById(id));
	}

	@ApiOperation(value = "查询问卷")
	@PostMapping("list")
	public ResultBean<Page<QuestionnaireVO>> list(@Valid @RequestBody QuestionnaireQueryParam param) throws CustomException {
		return ResultBean.ok(questionnaireService.queryPage(param));
	}

	@ApiOperation(value = "删除问卷")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		questionnaireService.softDelete(id);
		return ResultBean.ok();
	}

}
