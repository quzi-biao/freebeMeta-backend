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
import com.freebe.code.business.meta.entity.FilePermission;
import com.freebe.code.business.meta.service.FilePermissionService;
import com.freebe.code.business.meta.vo.FilePermissionVO;
import com.freebe.code.business.meta.controller.param.FilePermissionParam;
import com.freebe.code.business.meta.controller.param.FilePermissionQueryParam;
import com.freebe.code.business.meta.repository.FilePermissionRepository;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class FilePermissionServiceImpl extends BaseServiceImpl<FilePermission> implements FilePermissionService {
	@Autowired
	private FilePermissionRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public FilePermissionVO findById(Long id) throws CustomException {
		FilePermissionVO ret = this.objectCaches.get(id, FilePermissionVO.class);
		if(null == ret){
			Optional<FilePermission> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public FilePermissionVO createOrUpdate(FilePermissionParam param) throws CustomException {
		FilePermission e = this.getUpdateEntity(param);

		e.setFileId(param.getFileId());
		e.setUserId(param.getUserId());
		e.setTransactionId(param.getTransactionId());
		e.setPermissionType(param.getPermissionType());
		e.setPermissionTimes(param.getPermissionTimes());
		e.setViewTimes(param.getViewTimes());

		e = repository.save(e);

		FilePermissionVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<FilePermissionVO> queryPage(FilePermissionQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<FilePermission> example = buildSpec(param);

		Page<FilePermission> page = repository.findAll(example, request);
		List<FilePermissionVO> retList = new ArrayList<>();

		for(FilePermission e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<FilePermissionVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<FilePermission> buildSpec(FilePermissionQueryParam param) throws CustomException {
		return new Specification<FilePermission>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<FilePermission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<FilePermission> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private FilePermissionVO toVO(FilePermission e) throws CustomException {
		FilePermissionVO vo = new FilePermissionVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setFileId(e.getFileId());
		vo.setUserId(e.getUserId());
		vo.setTransactionId(e.getTransactionId());
		vo.setPermissionType(e.getPermissionType());
		vo.setPermissionTimes(e.getPermissionTimes());
		vo.setViewTimes(e.getViewTimes());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, FilePermissionVO.class);
		super.softDelete(id);
	}

}
