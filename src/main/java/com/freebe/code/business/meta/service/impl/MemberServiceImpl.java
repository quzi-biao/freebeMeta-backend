package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.business.meta.controller.param.MemberParam;
import com.freebe.code.business.meta.controller.param.MemberQueryParam;
import com.freebe.code.business.meta.controller.param.MemberRoleRelationParam;
import com.freebe.code.business.meta.controller.param.WalletQueryParam;
import com.freebe.code.business.meta.entity.Member;
import com.freebe.code.business.meta.repository.MemberRepository;
import com.freebe.code.business.meta.service.MemberRoleRelationService;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.service.ProjectMemberService;
import com.freebe.code.business.meta.service.RoleService;
import com.freebe.code.business.meta.service.WalletService;
import com.freebe.code.business.meta.service.impl.lucene.MemberLuceneSearch;
import com.freebe.code.business.meta.vo.MemberVO;
import com.freebe.code.business.meta.vo.RoleVO;
import com.freebe.code.business.meta.vo.Skill;
import com.freebe.code.business.meta.vo.WalletVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class MemberServiceImpl extends BaseServiceImpl<Member> implements MemberService {
	@Autowired
	private MemberRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MemberLuceneSearch searcher;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private ProjectMemberService projectMemberService;
	
	@Autowired
	private MemberRoleRelationService memberRoleRelationService;
	
	private Random r = new Random();
	
	@PostConstruct
	public void loadData() {
		Member probe = new Member();
		probe.setIsDelete(false);
		
		List<Member> members = this.repository.findAll(Example.of(probe));
		for(Member member : members) {
			objectCaches.put(member.getId(), member);
		}
	}
	
	@Override
	public Long countMembers() throws CustomException {
		Member probe = new Member();
		probe.setIsDelete(false);
		return this.repository.count(Example.of(probe));
	}

	@Override
	public MemberVO findById(Long id) throws CustomException {
		if(null == id) {
			return null;
		}
		Member ret = this.objectCaches.get(id, Member.class);
		if(null == ret){
			Optional<Member> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		
		return toVO(ret);
	}
	
	@Override
	public MemberVO findByUserId(Long userId) throws CustomException {
		if(null == userId) {
			return null;
		}
		String id = "user:" + userId;
		Member ret = this.objectCaches.get(id, Member.class);
		if(null == ret){
			Member probe = new Member();
			probe.setUserId(userId);
			probe.setIsDelete(false);
			Optional<Member> op = this.repository.findOne(Example.of(probe));
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(id, ret);
		return toVO(ret);
	}
	
	@Override
	public Long getMemberIdByUserId(Long userId) throws CustomException {
		if(null == userId) {
			throw new CustomException("参数错误");
		}
		String id = "user:" + userId;
		Member ret = this.objectCaches.get(id, Member.class);
		if(null == ret){
			Member probe = new Member();
			probe.setUserId(userId);
			probe.setIsDelete(false);
			Optional<Member> op = this.repository.findOne(Example.of(probe));
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		
		if(null == ret) {
			throw new CustomException("用户不存在");
		}
		
		return ret.getId();
	}
	
	@Override
	public MemberVO createOrUpdate(MemberParam param) throws CustomException {
		checkParam(param);
		
		UserVO user = getCurrentUser();
		MemberVO member = this.findByUserId(user.getId());
		
		// 已注册的用户，则更新原先的信息
		if(null != member) {
			param.setId(member.getId());
		}
		
		Member e = this.getUpdateEntity(param, false);
		e.setUserId(user.getId());
		e.setName(param.getName());
		e.setDescription(param.getDescription());
		e.setSkills(toStr(param.getSkills()));
		//e.setRoles(param.getRoles());
		userService.updateUserInfo(param.getName(), param.getAvator());

		e = repository.save(e);

		MemberVO vo = toVO(e);
		objectCaches.put(vo.getId(), e);
		objectCaches.put("user:" + e.getUserId(), e);
		searcher.addOrUpdateIndex(vo);

		return vo;
	}
	
	@Transactional
	@Override
	public MemberVO addRole(Long memberId, Long roleId) throws CustomException {
		Member member = this.getById(memberId);
		if(null == member) {
			throw new CustomException("成员不存在");
		}
		
		RoleVO role = this.roleService.findById(roleId);
		if(null == role) {
			throw new CustomException("角色不存在");
		}
		
		String roleStr = member.getRoles();
		Set<Long> roleIds = new HashSet<>();
		if(!StringUtils.isEmpty(roleStr)) {
			roleIds = new HashSet<>(toList(roleStr, Long.class));
		}
		
		MemberRoleRelationParam relationParam = new MemberRoleRelationParam();
		relationParam.setMemberId(memberId);
		relationParam.setRoleId(roleId);
		this.memberRoleRelationService.createOrUpdate(relationParam);
		
		roleIds.add(roleId);
		member.setRoles(JSONObject.toJSONString(roleIds));
		this.repository.save(member);
		
		MemberVO vo = toVO(member);
		objectCaches.put(vo.getId(), member);
		objectCaches.put("user:" + member.getUserId(), member);
		searcher.addOrUpdateIndex(vo);
		
		return vo;
	}

	@Override
	public Page<MemberVO> queryPage(MemberQueryParam param) throws CustomException {
		if("freeBe".equals(param.getOrder())) {
			// 积分排序的实现
			WalletQueryParam walletQuery = new WalletQueryParam();
			walletQuery.setOrder("freeBe");
			walletQuery.setDesc(param.getDesc());
			walletQuery.setCurrPage(param.getCurrPage());
			walletQuery.setLimit(param.getLimit());
			
			Page<WalletVO> wallets = this.walletService.queryPage(walletQuery);
			List<MemberVO> retList = new ArrayList<>();
			for(WalletVO e:  wallets.getContent()) {
				retList.add(this.findByUserId(e.getUserId()));
			}
			return new PageImpl<MemberVO>(retList, wallets.getPageable(), wallets.getTotalElements());
		}
		
		if(null == param.getKeyWords() || param.getKeyWords().length() == 0) {
			if(null == param.getOrder()) {
				param.setOrder("id");
			}
			List<MemberVO> retList = new ArrayList<>();
			PageRequest request = PageUtils.toPageRequest(param);
			if(StringUtils.isEmpty(param.getName())) {
				// 没有搜索条件的时候，随机返回一个列表
				retList = this.randomList(param.getLimit());
				return new PageImpl<MemberVO>(retList, request, this.countMembers());
			}else {
				// 根据条件查询
				Specification<Member> example = buildSpec(param);
				Page<Member> page = repository.findAll(example, request);
				for(Member e:  page.getContent()) {
					retList.add(toVO(e));
				}
				return new PageImpl<MemberVO>(retList, page.getPageable(), page.getTotalElements());
			}
			
		}else {
			if(param.getKeyWords().length() > 256) {
				throw new CustomException("查询关键词太长");
			}
			// 全文检索
			Page<MemberVO> page = searcher.fullTextSearch(param);
			List<MemberVO> retList = new ArrayList<>();
			for(MemberVO e:  page.getContent()) {
				retList.add(toVO(this.getById(e.getId())));
			}
			return new PageImpl<MemberVO>(retList, page.getPageable(), page.getTotalElements());
		}
	}


	private MemberVO toVO(Member e) throws CustomException {
		MemberVO vo = new MemberVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setUserId(e.getUserId());
		vo.setUser(userService.getUser(e.getUserId()));
		
		if(!vo.getUser().getName().equals(e.getName())) {
			Member m = this.repository.getById(e.getId());
			m.setName(vo.getUser().getName());
			this.repository.save(m);
		}
		
		vo.setName(vo.getUser().getName());
		vo.setAvator(vo.getUser().getAvator());		
		vo.setDescription(e.getDescription());
		vo.setSkills(toList(e.getSkills(), Skill.class));
		vo.setLastTime(e.getLastTime());
		vo.setRoles(roleService.findByIds(toList(e.getRoles(), Long.class)));
		
		WalletVO wallet = walletService.findByUser(e.getUserId());
		vo.setFreeBe(wallet.getFreeBe());
		vo.setJoinProjects(this.projectMemberService.countProject(e.getId()));

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		MemberVO vo = this.findById(id);
		searcher.deleteIndex(this.findById(id));
		objectCaches.delete(id, Member.class);
		objectCaches.delete("user:" + vo.getUserId(), Member.class);
		objectCaches.delete("freeBe:" + vo.getFreeBeId(), Member.class);
		super.softDelete(id);
	}
	
	private Specification<Member> buildSpec(MemberQueryParam param) throws CustomException {
		return new Specification<Member>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Member> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addEqual("name", param.getName());

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}
	
	private List<MemberVO> randomList(long size) throws CustomException {
		Long maxId = this.repository.getMaxId();
		List<MemberVO> retList = new ArrayList<>();
		long count = this.countMembers();
		if(size > count) {
			size = count;
		}
				
		if(count > 100) {
			Set<Long> retIds = new HashSet<>();
			while(size > 0) {
				Long id = (long)r.nextInt(maxId.intValue());
				if(retIds.contains(id)) {
					continue;
				}
				MemberVO member = this.findById(id);
				if(null != member) {
					retList.add(member);
				}else {
					continue;
				}
				size--;
			}
		}else {
			List<Long> ids = new ArrayList<>();
			
			for(int i = 0; i < maxId + 1; i++) {
				ids.add((long) i + 1);
			}
			
			while(size > 0) {
				if(ids.size() == 0) {
					break;
				}
				int index = r.nextInt(ids.size());
				long id = ids.remove(index);
				MemberVO member = this.findById(id);
				if(null != member) {
					retList.add(member);
				}else {
					continue;
				}
				
				size--;
			}
		}
		
		return retList;
	}
	

	private void checkParam(MemberParam param) throws CustomException {
		if(param.getName() == null || param.getName().length() == 0) {
			throw new CustomException("昵称不能为空");
		}
		
		if(param.getName().length() > 16) {
			throw new CustomException("名称不能超过16个字");
		}
		
		if(StringUtils.isEmpty(param.getDescription())) {
			throw new CustomException("请介绍下你自己");
		}
		
		if(param.getDescription().length() > 1024) {
			throw new CustomException("介绍不能超过1024个字");
		}
		
		if(null != param.getSkills()) {
			if(param.getSkills().size() > 16) {
				throw new CustomException("最多为自己添加16个标签");
			}
			for(Skill skill : param.getSkills()) {
				if(StringUtils.isEmpty(skill.getName())) {
					throw new CustomException("技能名称不能为空");
				}
				if(skill.getName().length() > 12) {
					throw new CustomException("技能标签不能超过12个字");
				}
			}
		}
	}

}
