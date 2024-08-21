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

import com.freebe.code.business.meta.service.QuestionnaireAnswerService;
import com.freebe.code.business.meta.vo.QuestionnaireAnswerVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.QuestionnaireAnswerParam;
import com.freebe.code.business.meta.controller.param.QuestionnaireAnswerQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/questionnaireanswer")
@CrossOrigin(origins = "*")
@Api(tags="问卷答题接口")
public class QuestionnaireAnswerController {
	@Autowired
	private QuestionnaireAnswerService questionnaireanswerService;

	@ApiOperation(value = "创建或者更新问卷答题")
	@PostMapping("createOrUpdate")
	public ResultBean<QuestionnaireAnswerVO> createOrUpdate(@Valid @RequestBody QuestionnaireAnswerParam param) throws CustomException {
		return ResultBean.ok(questionnaireanswerService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取问卷答题")
	@GetMapping("get/{id}")
	public ResultBean<QuestionnaireAnswerVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(questionnaireanswerService.findById(id));
	}
	
	@ApiOperation(value = "获取当前用户的回答")
	@GetMapping("curr/{questionnaireId}/{userId}")
	public ResultBean<QuestionnaireAnswerVO> getCurr(
			@PathVariable("questionnaireId") Long questionnaireId,
			@PathVariable("userId") Long userId) throws CustomException {
		return ResultBean.ok(questionnaireanswerService.getAnswer(questionnaireId, userId));
	}

	@ApiOperation(value = "查询问卷答题")
	@PostMapping("list")
	public ResultBean<Page<QuestionnaireAnswerVO>> list(@Valid @RequestBody QuestionnaireAnswerQueryParam param) throws CustomException {
		return ResultBean.ok(questionnaireanswerService.queryPage(param));
	}

	@ApiOperation(value = "删除问卷答题")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		questionnaireanswerService.softDelete(id);
		return ResultBean.ok();
	}
	
	@ApiOperation(value = "删除问卷答题")
	@GetMapping("public/{id}/{isPublic}")
	public ResultBean<Boolean> changePublic(@PathVariable("id") Long id, @PathVariable("isPublic") Boolean isPublic) throws CustomException {
		return ResultBean.ok(questionnaireanswerService.changePublic(id, isPublic));
	}

}
