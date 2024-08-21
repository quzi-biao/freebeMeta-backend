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

import com.freebe.code.business.meta.controller.param.MemberParam;
import com.freebe.code.business.meta.controller.param.MemberQueryParam;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.vo.MemberVO;
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
@RequestMapping("/member")
@CrossOrigin(origins = "*")
@Api(tags="成员接口")
public class MemberController {
	@Autowired
	private MemberService memberService;

	@ApiOperation(value = "创建或者更新成员")
	@PostMapping("createOrUpdate")
	public ResultBean<MemberVO> createOrUpdate(@Valid @RequestBody MemberParam param) throws CustomException {
		return ResultBean.ok(memberService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取成员")
	@GetMapping("get/{id}")
	public ResultBean<MemberVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(memberService.findByUserId(id));
	}
	
	@ApiOperation(value = "社区成员总数")
	@GetMapping("count")
	public ResultBean<Long> count() throws CustomException {
		return ResultBean.ok(memberService.countMembers());
	}

	@ApiOperation(value = "查询成员")
	@PostMapping("list")
	public ResultBean<Page<MemberVO>> list(@Valid @RequestBody MemberQueryParam param) throws CustomException {
		return ResultBean.ok(memberService.queryPage(param));
	}
	
	@ApiOperation(value = "添加角色")
	@GetMapping("addRole/{id}/{roleId}")
	public ResultBean<MemberVO> addRole(@PathVariable("id") Long id, @PathVariable("roleId") Long roleId) throws CustomException {
		return ResultBean.ok(memberService.addRole(id, roleId));
	}
}
