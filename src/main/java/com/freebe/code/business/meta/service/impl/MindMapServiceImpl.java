package com.freebe.code.business.meta.service.impl;


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

import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.MindMapParam;
import com.freebe.code.business.meta.controller.param.MindMapQueryParam;
import com.freebe.code.business.meta.entity.MindMap;
import com.freebe.code.business.meta.repository.MindMapRepository;
import com.freebe.code.business.meta.service.MindMapService;
import com.freebe.code.business.meta.service.ProjectService;
import com.freebe.code.business.meta.vo.MindMapVO;
import com.freebe.code.business.meta.vo.ProjectMemberVO;
import com.freebe.code.business.meta.vo.ProjectVO;
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
public class MindMapServiceImpl extends BaseServiceImpl<MindMap> implements MindMapService {
	@Autowired
	private MindMapRepository repository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public MindMapVO findById(Long id) throws CustomException {
		MindMapVO ret = this.objectCaches.get(id, MindMap.class);
		if(null == ret){
			Optional<MindMap> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			MindMap e = op.get();
			objectCaches.put(e.getId(), e);
			
			ret = toVO(e);
		}
		
		return ret;
	}

	@Override
	public MindMapVO createOrUpdate(MindMapParam param) throws CustomException {
		MindMap probe = new MindMap();
		probe.setProjectId(param.getProjectId());
		probe.setIsDelete(false);
		
		MindMap e = null;
		List<MindMap> list = this.repository.findAll(Example.of(probe));
		if(null == list || list.size() == 0) {
			e = this.getUpdateEntity(param, false);
		}else {
			e = list.get(0);
		}
		
		
		Long currId = getCurrentUser().getId();
		if(null == param.getProjectId()) {
			e.setOwnerId(currId);
		}else {
			ProjectVO project = this.projectService.findById(param.getProjectId());
			if(null == project) {
				throw new CustomException("项目不存在");
			}
			
			if(null == e.getProjectId()) {
				e.setProjectId(param.getProjectId());
			}else {
				// 项目所有者才有可能将思维导图修改到另一个项目
				if(currId.longValue() == project.getOwnerId().longValue()) {
					e.setProjectId(param.getProjectId());
				}
			}
			
			if(currId.longValue() != project.getOwnerId().longValue()) {
				boolean isMember = false;
				if(null == project.getMembers() || project.getMembers().size() == 0) {
					throw new CustomException("您不是项目组成员");
				}
				for(ProjectMemberVO member : project.getMembers()) {
					if(member.getMember().getUserId().longValue() == currId.longValue()) {
						isMember = true;
						break;
					}
				}
				if(!isMember) {
					throw new CustomException("您不是项目组成员");
				}
			}
		}
		
		e.setContent(param.getContent());

		e = repository.save(e);

		MindMapVO vo = toVO(e);
		objectCaches.put(e.getId(), e);

		return vo;
	}

	@Override
	public Page<MindMapVO> queryPage(MindMapQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<MindMap> example = buildSpec(param);

		Page<MindMap> page = repository.findAll(example, request);
		List<MindMapVO> retList = new ArrayList<>();

		for(MindMap e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<MindMapVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<MindMap> buildSpec(MindMapQueryParam param) throws CustomException {
		return new Specification<MindMap>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<MindMap> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<MindMap> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("projectId", param.getProjectId());
				builder.addEqual("ownerId", param.getOwnerId());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private MindMapVO toVO(MindMap e) throws CustomException {
		MindMapVO vo = new MindMapVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setOwner(this.userService.getUser(e.getOwnerId()));
		vo.setProject(this.projectService.findById(e.getProjectId()));
		vo.setContent(e.getContent());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, MindMap.class);
		super.softDelete(id);
	}

}
