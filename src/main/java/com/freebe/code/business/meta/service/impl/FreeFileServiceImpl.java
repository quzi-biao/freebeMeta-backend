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

import com.freebe.code.common.ObjectCaches;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.common.CustomException;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;
import com.freebe.code.business.meta.entity.FreeFile;
import com.freebe.code.business.meta.service.FreeFileService;
import com.freebe.code.business.meta.vo.FreeFileVO;
import com.freebe.code.business.meta.controller.param.FreeFileParam;
import com.freebe.code.business.meta.controller.param.FreeFileQueryParam;
import com.freebe.code.business.meta.repository.FreeFileRepository;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class FreeFileServiceImpl extends BaseServiceImpl<FreeFile> implements FreeFileService {
	@Autowired
	private FreeFileRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public FreeFileVO findById(Long id) throws CustomException {
		FreeFileVO ret = this.objectCaches.get(id, FreeFileVO.class);
		if(null == ret){
			Optional<FreeFile> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public FreeFileVO createOrUpdate(FreeFileParam param) throws CustomException {
		FreeFile e = this.getUpdateEntity(param);

		e.setTitle(param.getTitle());
		e.setDescription(param.getDescription());
		e.setFileSize(param.getFileSize());
		e.setUrl(param.getUrl());
		e.setFileType(param.getFileType());
		e.setPublicType(param.getPublicType());
		e.setOwnerId(param.getOwnerId());
		e.setPrice(param.getPrice());
		e.setPriceCategory(param.getPriceCategory());

		e = repository.save(e);

		FreeFileVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<FreeFileVO> queryPage(FreeFileQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<FreeFile> example = buildSpec(param);

		Page<FreeFile> page = repository.findAll(example, request);
		List<FreeFileVO> retList = new ArrayList<>();

		for(FreeFile e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<FreeFileVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<FreeFile> buildSpec(FreeFileQueryParam param) throws CustomException {
		return new Specification<FreeFile>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<FreeFile> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<FreeFile> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private FreeFileVO toVO(FreeFile e) throws CustomException {
		FreeFileVO vo = new FreeFileVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setTitle(e.getTitle());
		vo.setDescription(e.getDescription());
		vo.setFileSize(e.getFileSize());
		vo.setUrl(e.getUrl());
		vo.setFileType(e.getFileType());
		vo.setPublicType(e.getPublicType());
		vo.setOwnerId(e.getOwnerId());
		vo.setPrice(e.getPrice());
		vo.setPriceCategory(e.getPriceCategory());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, FreeFileVO.class);
		super.softDelete(id);
	}

}
