package com.freebe.code.business.code.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.business.code.BuilderSession;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/code")
@CrossOrigin(origins = "*")
@Api(tags = "代码模块")
public class PromptController {
	
	@Autowired
	private BuilderSession session;
	
    @PostMapping("submit")
    public ResultBean<String> login(@Valid @RequestBody PromptParam param) throws CustomException {
    	session.updatePage(param.getText());
    	
        return ResultBean.ok("", "");
    }
}
