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

import com.freebe.code.business.meta.controller.param.MessageParam;
import com.freebe.code.business.meta.controller.param.MessageQueryParam;
import com.freebe.code.business.meta.service.MessageService;
import com.freebe.code.business.meta.vo.MessageVO;
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
@RequestMapping("/message")
@CrossOrigin(origins = "*")
@Api(tags="消息接口")
public class MessageController {
	@Autowired
	private MessageService messageService;

	@ApiOperation(value = "创建或者更新消息")
	@PostMapping("createOrUpdate")
	public ResultBean<MessageVO> createOrUpdate(@Valid @RequestBody MessageParam param) throws CustomException {
		return ResultBean.ok(messageService.createOrUpdate(param));
	}

	@ApiOperation(value = "查询消息")
	@PostMapping("list")
	public ResultBean<Page<MessageVO>> list(@Valid @RequestBody MessageQueryParam param) throws CustomException {
		return ResultBean.ok(messageService.queryPage(param));
	}
	
	@ApiOperation(value = "查询未读消息的数量")
	@GetMapping("count")
	public ResultBean<Long> unread() throws CustomException {
		return ResultBean.ok(messageService.countUnRead());
	}
	
	@ApiOperation(value = "读消息, 1 表示接受，0表示拒绝")
	@GetMapping("read/{msgId}/{param}")
	public ResultBean<MessageVO> read(@PathVariable("msgId") Long msgId, @PathVariable("param") String param) throws CustomException {
		return ResultBean.ok(messageService.readMsg(msgId, param));
	}

}
