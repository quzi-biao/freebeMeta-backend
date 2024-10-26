package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;

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

import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.RichTextParam;
import com.freebe.code.business.meta.controller.param.RichTextQueryParam;
import com.freebe.code.business.meta.entity.RichText;
import com.freebe.code.business.meta.repository.RichTextRepository;
import com.freebe.code.business.meta.service.RichTextService;
import com.freebe.code.business.meta.vo.RichTextVO;
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
public class RichTextServiceImpl extends BaseServiceImpl<RichText> implements RichTextService {
	@Autowired
	private RichTextRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public RichTextVO findById(String cliendId) throws CustomException {
		RichTextVO ret = this.objectCaches.get(cliendId, RichTextVO.class);
		if(null == ret){
			RichText rt = new RichText();
			rt.setIsDelete(false);
			rt.setClientId(cliendId);
			
			List<RichText> match = this.repository.findAll(Example.of(rt));
			
			if(null == match || match.size() == 0) {
				return null;
			}
			
			ret = toVO(match.get(0));
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public RichTextVO createOrUpdate(RichTextParam param) throws CustomException {
		RichText e = null;
		if(null == param.getClientId()) {
			e = this.getUpdateEntity(param, false);
		}else {
			RichText rt = new RichText();
			rt.setIsDelete(false);
			rt.setClientId(param.getClientId());
			
			List<RichText> match = this.repository.findAll(Example.of(rt));
			if(null == match || match.size() == 0) {
				e = this.getUpdateEntity(param, false);
			}else {
				e = match.get(0);
			}
		}

		e.setOwnerId(this.getCurrentUser().getId());
		e.setOwnerType(param.getOwnerType());
		e.setByteContent(param.getByteContent());
		e.setTextContent(param.getTextContent());
		e.setContentType(param.getContentType());
		e.setClientId(param.getClientId());

		e = repository.save(e);

		RichTextVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<RichTextVO> queryPage(RichTextQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<RichText> example = buildSpec(param);

		Page<RichText> page = repository.findAll(example, request);
		List<RichTextVO> retList = new ArrayList<>();

		for(RichText e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<RichTextVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<RichText> buildSpec(RichTextQueryParam param) throws CustomException {
		return new Specification<RichText>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<RichText> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<RichText> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private RichTextVO toVO(RichText e) throws CustomException {
		RichTextVO vo = new RichTextVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setOwnerId(e.getOwnerId());
		vo.setOwnerType(e.getOwnerType());
		vo.setByteContent(e.getByteContent());
		vo.setTextContent(e.getTextContent());
		vo.setContentType(e.getContentType());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, RichTextVO.class);
		super.softDelete(id);
	}

}
