package com.freebe.code.business.badge.controller;

import java.util.List;

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

import com.freebe.code.business.badge.controller.param.BadgeActionParam;
import com.freebe.code.business.badge.controller.param.BadgeActionRecordQueryParam;
import com.freebe.code.business.badge.controller.param.BadgeHoldQueryParam;
import com.freebe.code.business.badge.controller.param.BadgeParam;
import com.freebe.code.business.badge.controller.param.BadgeQueryParam;
import com.freebe.code.business.badge.service.BadgeActionRecordService;
import com.freebe.code.business.badge.service.BadgeHoldService;
import com.freebe.code.business.badge.service.BadgeService;
import com.freebe.code.business.badge.vo.BadgeActionRecordVO;
import com.freebe.code.business.badge.vo.BadgeHoldVO;
import com.freebe.code.business.badge.vo.BadgeVO;
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
@RequestMapping("/badge")
@CrossOrigin(origins = "*")
@Api(tags="徽章接口")
public class BadgeController {
	@Autowired
	private BadgeService badgeService;
	
	@Autowired
	private BadgeActionRecordService badgeactionrecordService;
	
	@Autowired
	private BadgeHoldService badgeholdService;

	@ApiOperation(value = "查询徽章持有者列表")
	@GetMapping("member/{badgeId}")
	public ResultBean<List<BadgeHoldVO>> getBadgeHolder(@PathVariable("badgeId") Long badgeId) throws CustomException {
		BadgeHoldQueryParam param = new BadgeHoldQueryParam();
		param.setBadgeId(badgeId);
		return ResultBean.ok(badgeholdService.queryHold(param));
	}
	
	@ApiOperation(value = "查询某个成员持有的徽章")
	@GetMapping("hold/{memberId}")
	public ResultBean<List<BadgeHoldVO>> getHold(@PathVariable("memberId") Long memberId) throws CustomException {
		BadgeHoldQueryParam param = new BadgeHoldQueryParam();
		param.setMemberId(memberId);
		return ResultBean.ok(badgeholdService.queryHold(param));
	}
	
	@ApiOperation(value = "发放徽章")
	@PostMapping("award")
	public ResultBean<BadgeHoldVO> award(@Valid @RequestBody BadgeActionParam param) throws CustomException {
		return ResultBean.ok(badgeholdService.awardBadge(param));
	}
	
	@ApiOperation(value = "回收徽章")
	@PostMapping("takeback")
	public ResultBean<BadgeHoldVO> takeBack(@Valid @RequestBody BadgeActionParam param) throws CustomException {
		return ResultBean.ok(badgeholdService.takeBackBadge(param));
	}
	
	@ApiOperation(value = "回收徽章")
	@PostMapping("giveout")
	public ResultBean<BadgeHoldVO> giveOut(@Valid @RequestBody BadgeActionParam param) throws CustomException {
		return ResultBean.ok(badgeholdService.giveOutBadge(param));
	}

	@ApiOperation(value = "查询徽章操作记录")
	@PostMapping("actions")
	public ResultBean<Page<BadgeActionRecordVO>> actionRecord(@Valid @RequestBody BadgeActionRecordQueryParam param) throws CustomException {
		return ResultBean.ok(badgeactionrecordService.queryPage(param));
	}

	@ApiOperation(value = "创建或者更新徽章")
	@PostMapping("createOrUpdate")
	public ResultBean<BadgeVO> createOrUpdate(@Valid @RequestBody BadgeParam param) throws CustomException {
		return ResultBean.ok(badgeService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取徽章")
	@GetMapping("get/{id}")
	public ResultBean<BadgeVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(badgeService.findById(id));
	}

	@ApiOperation(value = "查询徽章")
	@PostMapping("list")
	public ResultBean<Page<BadgeVO>> list(@Valid @RequestBody BadgeQueryParam param) throws CustomException {
		return ResultBean.ok(badgeService.queryPage(param));
	}

	@ApiOperation(value = "删除徽章")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		badgeService.softDelete(id);
		return ResultBean.ok();
	}

}
