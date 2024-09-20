package com.freebe.code.business.advanture.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.freebe.code.business.advanture.controller.param.AdvantureCardParam;
import com.freebe.code.business.advanture.controller.param.AdvantureCardQueryParam;
import com.freebe.code.business.advanture.entity.AdvantureCard;
import com.freebe.code.business.advanture.repository.AdvantureCardRepository;
import com.freebe.code.business.advanture.service.AdvantureCardService;
import com.freebe.code.business.advanture.type.CardState;
import com.freebe.code.business.advanture.type.Constant;
import com.freebe.code.business.advanture.vo.AdvantureCardVO;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
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
public class AdvantureCardServiceImpl extends BaseServiceImpl<AdvantureCard> implements AdvantureCardService {
	@Autowired
	private AdvantureCardRepository repository;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public AdvantureCardVO findByUserId(Long userId) throws CustomException {
		if(null == userId) {
			userId = this.getCurrentUser().getId();
		}
		AdvantureCard ret = this.objectCaches.get(userId, AdvantureCard.class);
		if(null == ret){
			AdvantureCard e = new AdvantureCard();
			e.setUserId(userId);
			
			Optional<AdvantureCard> op = this.repository.findOne(Example.of(e));
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(userId, ret);
		return toVO(ret);
	}

	@Override
	public synchronized AdvantureCardVO createOrUpdate(AdvantureCardParam param) throws CustomException {
		Long userId = getCurrentUser().getId();
		param.setName("adcard-" + userId);
		
		AdvantureCard e = this.getUpdateEntity(param);

		e.setUserId(userId);
		e.setStartTime(System.currentTimeMillis());
		e.setEndTime(System.currentTimeMillis() + Constant.TEST_TIME);
		e.setExperience(0L);

		e = repository.save(e);

		AdvantureCardVO vo = toVO(e);
		objectCaches.put(e.getUserId(), e);

		return vo;
	}
	
	@Override
	public AdvantureCardVO addExperience(Long id, Long added) throws CustomException {
		
		AdvantureCard e = new AdvantureCard();
		e.setUserId(id);
		
		Optional<AdvantureCard> op = this.repository.findOne(Example.of(e));
		if(!op.isPresent()){
			return null;
		}
		AdvantureCard card = op.get();
		
		card.setExperience(card.getExperience() + added);
		
		objectCaches.put(card.getUserId(), card);
		
		return toVO(card);
	}

	@Override
	public Page<AdvantureCardVO> queryPage(AdvantureCardQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<AdvantureCard> example = buildSpec(param);

		Page<AdvantureCard> page = repository.findAll(example, request);
		List<AdvantureCardVO> retList = new ArrayList<>();

		for(AdvantureCard e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<AdvantureCardVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<AdvantureCard> buildSpec(AdvantureCardQueryParam param) throws CustomException {
		return new Specification<AdvantureCard>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<AdvantureCard> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<AdvantureCard> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				// 查询状态
				if(param.getState() != null) {
					if(param.getState().intValue() == CardState.PASS) {
						builder.addBetween("experience", Constant.EXPERIENCE, null);
					}else if(param.getState().intValue() == CardState.TESTING) {
						builder.addBetween("endTime", System.currentTimeMillis(), null);
					}else if(param.getState().intValue() == CardState.UNPASS) {
						builder.addBetween("endTime", null, System.currentTimeMillis());
					}
				}

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private AdvantureCardVO toVO(AdvantureCard e) throws CustomException {
		AdvantureCardVO vo = new AdvantureCardVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setUserId(e.getUserId());
		vo.setStartTime(e.getStartTime());
		vo.setEndTime(e.getEndTime());
		vo.setExperience(e.getExperience());
		vo.setUser(userService.getUser(e.getUserId()));
		
		if(e.getExperience() >= Constant.EXPERIENCE) {
			vo.setState(CardState.PASS);
		}else {
			if(e.getEndTime() <= System.currentTimeMillis()) {
				vo.setState(CardState.UNPASS);
			}else {
				vo.setState(CardState.TESTING);
			}
		}

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, AdvantureCardVO.class);
		super.softDelete(id);
	}

}
