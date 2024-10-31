package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.MarkerProvideParam;
import com.freebe.code.business.meta.controller.param.MarkerProvideQueryParam;
import com.freebe.code.business.meta.controller.param.MarketProvideApplyParam;
import com.freebe.code.business.meta.entity.MarketProvide;
import com.freebe.code.business.meta.entity.MarketProvideUser;
import com.freebe.code.business.meta.repository.MarketProvideRepository;
import com.freebe.code.business.meta.repository.MarketProvideUserRepository;
import com.freebe.code.business.meta.service.CollectService;
import com.freebe.code.business.meta.service.MarketProvideService;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.service.impl.lucene.MarketProvideLuceneSearch;
import com.freebe.code.business.meta.type.InteractionEntityType;
import com.freebe.code.business.meta.vo.MarketProvideVO;
import com.freebe.code.business.meta.vo.MarketProviderVO;
import com.freebe.code.business.meta.vo.MemberVO;
import com.freebe.code.business.meta.vo.RoleVO;
import com.freebe.code.common.CommonExecutor;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
@Slf4j
public class MarketProvideServiceImpl extends BaseServiceImpl<MarketProvide> implements MarketProvideService {
	@Autowired
	private MarketProvideRepository repository;
	
	@Autowired
	private MarketProvideUserRepository marketProvideUserRepository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CollectService collectService;
	
	@Autowired
	private MarketProvideLuceneSearch searcher;
	
	private Map<Long, MarketProvideUser> providerCache = new HashMap<>();
	
	@PostConstruct
	public void loadData() {
		CommonExecutor.execute(new Runnable() {
			
			@Override
			public void run() {
				CommonExecutor.sleep(20000);
				log.info("重新构建服务索引");
				MarketProvide probe = new MarketProvide();
				probe.setIsDelete(false);

				List<MarketProvide> provides = repository.findAll(Example.of(probe));
				for(MarketProvide provide : provides) {
					objectCaches.put(provide.getId(), provide);
					try {
						MarketProvideVO vo = new MarketProvideVO();
						vo.setId(provide.getId());
						vo.setDescription(provide.getDescription());
						vo.setTitle(provide.getTitle());
						vo.setTags(toList(provide.getTags()));
						vo.setCreateTime(provide.getCreateTime());						
						searcher.addOrUpdateIndex(vo);
					} catch (CustomException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public MarketProvideVO findById(Long id) throws CustomException {
		MarketProvide ret = this.objectCaches.get(id, MarketProvide.class);
		if(null == ret){
			Optional<MarketProvide> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return toVO(ret);
	}

	@Transactional
	@Override
	public MarketProvideVO createOrUpdate(MarkerProvideParam param) throws CustomException {
		checkParam(param);
		MarketProvide e = this.getUpdateEntity(param, false);

		if(null == e.getCreator()) {
			e.setCreator(getCurrentUser().getId());
		}
		
		if(e.getId() == null || e.getAudit() == null) {
			e.setAudit(true);
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
		
		MarketProvideVO vo = toVO(e);
		objectCaches.put(vo.getId(), e);
		
		searcher.addOrUpdateIndex(vo);

		return vo;
	}
	
	@Override
	public MarketProvideVO applyToProvider(MarketProvideApplyParam applyParam) throws CustomException {
		Long provideId = applyParam.getProvideId();
		if(null == provideId) {
			return null;
		}
		MarketProvideUser e = getMarketProvider(provideId);
	
		if(e == null) {
			e = new MarketProvideUser();
		}
		
		MarketProvide provide = this.getById(provideId);
		if(null == provide) {
			return null;
		}
		
		Long userId = getCurrentUser().getId();
		e.setUserId(userId);
		e.setProvideId(provideId);
		e.setIsDelete(false);
		
		e.setEvidence(applyParam.getEvidence());
		e.setExperience(applyParam.getExperience());
		e.setServiceTime(applyParam.getServiceTime());
		
		e = this.marketProvideUserRepository.save(e);
		
		List<Long> providers = toList(provide.getProviders(), Long.class);
		if(null == providers) {
			providers = new ArrayList<>();
		}
		
		if(providers.indexOf(e.getId()) < 0) {
			providers.add(e.getId());
			provide.setProviders(toStr(providers));
			provide = this.repository.save(provide);
		}
		
		this.providerCache.put(e.getId(), e);
		
		return toVO(provide);
	}
	
	@Override
	public MarketProvideVO giveoutProvider(Long provideId) throws CustomException {
		if(null == provideId) {
			return null;
		}
		MarketProvideUser e = getMarketProvider(provideId);
		if(null == e) {
			return null;
		}
		
		MarketProvide provide = this.getById(provideId);
		if(null == provide) {
			return null;
		}
		
		e.setIsDelete(true);
		
		this.marketProvideUserRepository.save(e);
		
		List<Long> providers = toList(provide.getProviders(), Long.class);
		if(null == providers) {
			providers = new ArrayList<>();
		}
		
		providers.remove(e.getId());
		provide.setProviders(toStr(providers));
		provide = this.repository.save(provide);
		
		this.providerCache.remove(e.getId());
		
		return toVO(provide);
	}
	
	@Override
	public MarketProvideVO auditProvider(Long providerId) throws CustomException {
		if(!checkFinanceOfficer()) {
			throw new CustomException("您没有审核权限");
		}
		MarketProvide provider = this.getById(providerId);
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
		
		MarketProvide provider = this.getById(providerId);
		if(null == provider) {
			throw new CustomException("供应不存在");
		}
		
		return provider.getContact();
	}
	
	@Override
	public Page<MarketProvideVO> queryMineProvide(MarkerProvideQueryParam param) throws CustomException {
		Long userId = getCurrentUser().getId();
		MarketProvideUser probe = new MarketProvideUser();
		probe.setUserId(userId);
		probe.setIsDelete(false);
		
		PageRequest request = PageUtils.toPageRequest(param);
		Page<MarketProvideUser> page = this.marketProvideUserRepository.findAll(Example.of(probe), request);
		List<MarketProvideVO> retList = new ArrayList<>();

		for(MarketProvideUser e:  page.getContent()) {
			retList.add(this.findById(e.getProvideId()));
		}
		return new PageImpl<MarketProvideVO>(retList, page.getPageable(), page.getTotalElements());
	}

	@Override
	public Page<MarketProvideVO> queryPage(MarkerProvideQueryParam param) throws CustomException {
		param.setOrder("id");
		if(param.getAudit() == null) {
			param.setAudit(true);
		}else {
			if(!this.checkFinanceOfficer()) {
				param.setAudit(true);
			}
		}
		
		if(null != param.getKeyWords() && !param.getKeyWords().isEmpty()){
			Page<MarketProvideVO> searchPage = searcher.fullTextSearch(param);
			List<Long> idList = new ArrayList<>();
			for(MarketProvideVO e:  searchPage.getContent()) {
				idList.add(e.getId());
			}
			if(idList.isEmpty()) {
				idList.add(-1L);
			}
			param.setIdList(idList);
		}
		
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<MarketProvide> example = buildSpec(param);

		Page<MarketProvide> page = repository.findAll(example, request);
		List<MarketProvideVO> retList = new ArrayList<>();

		for(MarketProvide e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<MarketProvideVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<MarketProvide> buildSpec(MarkerProvideQueryParam param) throws CustomException {
		return new Specification<MarketProvide>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<MarketProvide> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<MarketProvide> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("audit", param.getAudit());
				builder.addEqual("ownerId", param.getOwnerId());
				builder.addBetween("minPrice", param.getMinPrice(), param.getMaxPrice());
				builder.addBetween("maxPrice", param.getMinPrice(), param.getMaxPrice());
				builder.addLike("tags", param.getTags());
				
				builder.addIn("id", param.getIdList());

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}
	
	private MarketProvideUser getMarketProvider(Long provideId) {
		Long userId = getCurrentUser().getId();
		MarketProvideUser e = new MarketProvideUser();
		e.setUserId(userId);
		e.setProvideId(provideId);
		//e.setIsDelete(false);
		
		List<MarketProvideUser> finds = this.marketProvideUserRepository.findAll(Example.of(e));
		if(null == finds || finds.size() == 0) {
			return null;
		}
		
		return finds.get(0);
	}

	private MarketProvideVO toVO(MarketProvide e) throws CustomException {
		MarketProvideVO vo = new MarketProvideVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());
		
		vo.setCreator(userService.getUser(e.getCreator()));
		List<Long> providerIds = toList(e.getProviders(), Long.class);
		if(null != providerIds && providerIds.size() > 0) {
			List<MarketProviderVO> providers = new ArrayList<>();
			for(Long id : providerIds) {
				MarketProviderVO provider = this.getMarketProviderById(id);
				if(null != provider) {
					providers.add(provider);
				}
			}
			MarketProviderVO provider = new MarketProviderVO();
			provider.setUser(vo.getCreator());
			providers.add(provider);
			vo.setProviders(providers);
		}else {
			List<MarketProviderVO> providers = new ArrayList<>();
			MarketProviderVO provider = new MarketProviderVO();
			provider.setUser(vo.getCreator());
			providers.add(provider);
			vo.setProviders(providers);
		}
		
		vo.setTitle(e.getTitle());
		vo.setPicture(e.getPicture());
		vo.setDescription(e.getDescription());
		vo.setMaxPrice(e.getMaxPrice());
		vo.setMinPrice(e.getMinPrice());
		vo.setPriceDescription(e.getPriceDescription());
		vo.setTags(toList(e.getTags()));
		vo.setAudit(e.getAudit());
		//vo.setContact(e.getContact());
		vo.setCollected(this.collectService.isCollect(InteractionEntityType.PROVIDE, e.getId()));

		return vo;
	}


	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, MarketProvideVO.class);
		super.softDelete(id);
	}
	
	private MarketProviderVO getMarketProviderById(Long id) {
		MarketProvideUser provider = this.providerCache.get(id);
		if(null == provider) {
			provider = this.marketProvideUserRepository.getById(id);
		}else {
			return toProviderVO(provider);
		}
		
		this.providerCache.put(id, provider);
		
		return toProviderVO(provider);
	}

	
	private MarketProviderVO toProviderVO(MarketProvideUser e) {
		MarketProviderVO vo = new MarketProviderVO();
		
		vo.setEvidence(e.getEvidence());
		vo.setExperience(e.getExperience());
		vo.setServiceTime(e.getServiceTime());
		vo.setUser(this.userService.getUser(e.getUserId()));
		
		return vo;
	}

	private void checkParam(MarkerProvideParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getTitle())) {
			throw new CustomException("服务标题不能为空");
		}
		if(param.getTitle().length() > 256) {
			throw new CustomException("服务标题不能超过64个字符");
		}
		
		if(StringUtils.isEmpty(param.getDescription())) {
			throw new CustomException("服务描述不能为空");
		}
	
		if(StringUtils.isEmpty(param.getPicture())) {
			throw new CustomException("服务宣传图不能为空:您可以联系社区的设计师帮您画一张");
		}
		
//		if(param.getMaxPrice() == null || param.getMinPrice() == null) {
//			throw new CustomException("服务价格不能为空: 最高最低相同就是等于");
//		}
		
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
		if(null == member) {
			return false;
		}
		
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
