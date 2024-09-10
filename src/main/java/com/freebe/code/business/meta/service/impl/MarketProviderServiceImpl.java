package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.MarkerProviderParam;
import com.freebe.code.business.meta.controller.param.MarkerProviderQueryParam;
import com.freebe.code.business.meta.entity.MarketProvider;
import com.freebe.code.business.meta.repository.MarkerProviderRepository;
import com.freebe.code.business.meta.service.MarketProviderService;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.vo.MarketProviderVO;
import com.freebe.code.business.meta.vo.MemberVO;
import com.freebe.code.business.meta.vo.RoleVO;
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
public class MarketProviderServiceImpl extends BaseServiceImpl<MarketProvider> implements MarketProviderService {
	@Autowired
	private MarkerProviderRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MemberService memberService;

	@Override
	public MarketProviderVO findById(Long id) throws CustomException {
		MarketProvider ret = this.objectCaches.get(id, MarketProvider.class);
		if(null == ret){
			Optional<MarketProvider> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return toVO(ret);
	}

	@Override
	public MarketProviderVO createOrUpdate(MarkerProviderParam param) throws CustomException {
		checkParam(param);
		MarketProvider e = this.getUpdateEntity(param, false);

		e.setCreator(getCurrentUser().getId());
		if(null == param.getOwnerId()) {
			e.setOwnerId(e.getCreator());
		}else {
			e.setOwnerId(param.getOwnerId());
		}
		if(e.getId() == null || e.getAudit() == null) {
			e.setAudit(false);
		}
		e.setTitle(param.getTitle());
		e.setPicture(param.getPicture());
		e.setDescription(param.getDescription());
		e.setMaxPrice(param.getMaxPrice());
		e.setMinPrice(param.getMinPrice());
		e.setPriceDescription(param.getPriceDescription());
		e.setTags(toStr(param.getTags()));
		e.setContact(param.getContact());

		e = repository.save(e);

		MarketProviderVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}
	
	@Override
	public MarketProviderVO auditProvider(Long providerId) throws CustomException {
		if(!checkFinanceOfficer()) {
			throw new CustomException("您没有审核权限");
		}
		MarketProvider provider = this.getById(providerId);
		if(null == provider) {
			throw new CustomException("供应不存在");
		}
		
		provider.setAudit(true);
		provider = this.repository.save(provider);
		
		this.objectCaches.put(provider.getId(), provider);
		
		return toVO(provider);
	}
	
	@Override
	public String getContact(Long providerId) throws CustomException {
		Long userId = this.getCurrentUser().getId();
		MemberVO member = this.memberService.findByUserId(userId);
		
		if(null == member) {
			throw new CustomException("您还不是社区成员");
		}
		
		MarketProvider provider = this.getById(providerId);
		if(null == provider) {
			throw new CustomException("供应不存在");
		}
		
		return provider.getContact();
	}

	@Override
	public Page<MarketProviderVO> queryPage(MarkerProviderQueryParam param) throws CustomException {
		param.setOrder("id");
		if(param.getAudit() == null) {
			param.setAudit(true);
		}else {
			if(!this.checkFinanceOfficer()) {
				param.setAudit(true);
			}
		}
		
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<MarketProvider> example = buildSpec(param);

		Page<MarketProvider> page = repository.findAll(example, request);
		List<MarketProviderVO> retList = new ArrayList<>();

		for(MarketProvider e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<MarketProviderVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<MarketProvider> buildSpec(MarkerProviderQueryParam param) throws CustomException {
		return new Specification<MarketProvider>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<MarketProvider> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<MarketProvider> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("audit", param.getAudit());
				builder.addEqual("ownerId", param.getOwnerId());
				builder.addBetween("minPrice", param.getMinPrice(), param.getMaxPrice());
				builder.addBetween("maxPrice", param.getMinPrice(), param.getMaxPrice());
				builder.addLike("tags", param.getTags());

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private MarketProviderVO toVO(MarketProvider e) throws CustomException {
		MarketProviderVO vo = new MarketProviderVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setOwner(userService.getUser(e.getOwnerId()));
		vo.setCreator(userService.getUser(e.getCreator()));
		vo.setTitle(e.getTitle());
		vo.setPicture(e.getPicture());
		vo.setDescription(e.getDescription());
		vo.setMaxPrice(e.getMaxPrice());
		vo.setMinPrice(e.getMinPrice());
		vo.setPriceDescription(e.getPriceDescription());
		vo.setTags(toList(e.getTags()));
		vo.setAudit(e.getAudit());
		//vo.setContact(e.getContact());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, MarketProviderVO.class);
		super.softDelete(id);
	}
	
	private void checkParam(MarkerProviderParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getTitle())) {
			throw new CustomException("服务标题不能为空");
		}
		if(param.getTitle().length() > 256) {
			throw new CustomException("服务标题不能超过64个字符");
		}
		
		if(StringUtils.isEmpty(param.getDescription())) {
			throw new CustomException("服务描述不能为空");
		}
		
		if(param.getOwnerId() == null) {
			throw new CustomException("服务所有者不能为空");
		}
		
		if(StringUtils.isEmpty(param.getPicture())) {
			throw new CustomException("服务宣传图不能为空:您可以联系社区的设计师帮您画一张");
		}
		
		if(param.getMaxPrice() == null || param.getMinPrice() == null) {
			throw new CustomException("服务价格不能为空: 最高最低相同就是等于");
		}
		
		if(param.getTags() == null || param.getTags().size() == 0) {
			throw new CustomException("请设置服务标签");
		}
	}
	
	private boolean checkFinanceOfficer() throws CustomException {
		if(null == this.getCurrentUser()) {
			return false;
		}
		Long currUser = this.getCurrentUser().getId();
		MemberVO member = this.memberService.findByUserId(currUser);
		
		if(null == member.getRoles() || member.getRoles().size() == 0) {
			return false;
		}
		
		for(RoleVO role : member.getRoles()) {
			if(TransactionService.ROLE_CF_CODE.equals(role.getRoleCode())) {
				return true;
			}
		}
		return false;
	}
}
