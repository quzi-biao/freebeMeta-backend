package com.freebe.code.business.meta.vo;

import java.util.List;

import com.freebe.code.business.badge.vo.BadgeVO;
import com.freebe.code.business.base.vo.BaseVO;
import com.freebe.code.business.base.vo.UserVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 *
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel("成员")
public class MemberVO extends BaseVO {
	@ApiModelProperty("头像")
	private String avator;
	
	@ApiModelProperty("用户 ID")
	private Long userId;
	
	@ApiModelProperty("用户/账号")
	private UserVO user;

	@ApiModelProperty("邮箱")
	private String email;

	@ApiModelProperty("FreeBe ID")
	private String freeBeId;

	@ApiModelProperty("简介")
	private String description;

	@ApiModelProperty("技能树")
	private List<Skill> skills;

	@ApiModelProperty("最近的登录时间")
	private Long lastTime;

	@ApiModelProperty("角色列表")
	private List<RoleVO> roles;

	@ApiModelProperty("FreeBe积分")
	private Double freeBe;
	
	@ApiModelProperty("参与的项目")
	private Integer joinProjects;

	@ApiModelProperty("社区贡献")
	private Long contribution;
	
	@ApiModelProperty("成员邀请者")
	private Long invitor;
	
	@ApiModelProperty("持有的徽章")
	private List<BadgeVO> holdBadges;
}
