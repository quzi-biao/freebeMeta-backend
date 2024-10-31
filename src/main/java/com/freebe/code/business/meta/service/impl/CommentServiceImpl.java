package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.freebe.code.business.meta.controller.param.CommentParam;
import com.freebe.code.business.meta.controller.param.CommentQueryParam;
import com.freebe.code.business.meta.entity.Comment;
import com.freebe.code.business.meta.repository.CommentRepository;
import com.freebe.code.business.meta.service.CollectService;
import com.freebe.code.business.meta.service.CommentService;
import com.freebe.code.business.meta.service.ContentDataService;
import com.freebe.code.business.meta.service.ContentService;
import com.freebe.code.business.meta.service.LikeService;
import com.freebe.code.business.meta.type.ContentType;
import com.freebe.code.business.meta.type.InteractionEntityType;
import com.freebe.code.business.meta.type.InteractionType;
import com.freebe.code.business.meta.type.MessageType;
import com.freebe.code.business.meta.vo.CommentVO;
import com.freebe.code.business.meta.vo.ContentVO;
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
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements CommentService {
	@Autowired
	private CommentRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private ContentDataService contentDataService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private InteractionCountUtils countUtils;
	
	@Autowired
	private LikeService likeService;
	
	@Autowired
	private CollectService collectService;
	
	@Autowired
	private CommentService commentService;

	@Override
	public CommentVO findById(Long id) throws CustomException {
		Comment ret = getEntity(id);
		return toVO(ret);
	}

	@Transactional
	@Override
	public CommentVO createOrUpdate(CommentParam param) throws CustomException {
		if(null == param.getContentId()) {
			throw new CustomException("参数错误");
		}
		
		if(StringUtils.isEmpty(param.getContent())) {
			throw new CustomException("内容为空");
		}
		
		ContentVO content = this.contentService.findById(param.getContentId());
		
		Comment e = this.getUpdateEntity(param, false);

		e.setOwnerId(getCurrentUser().getId());
		e.setContentId(param.getContentId());
		e.setParentId(param.getParentId());
		if(null != e.getParentId()) {
			Comment parent = this.getEntity(e.getParentId());
			if(null == parent) {
				throw new CustomException("回复内容不存在");
			}
			e.setParentOwnerId(parent.getOwnerId());
			this.sendMessage(e.getOwnerId(), parent.getOwnerId(), "您的评论有新的回复", MessageType.CONTENT_COMMENT_MSG);
			countUtils.inc(e.getParentId(), (long) InteractionEntityType.COMMENT, InteractionType.COMMENT);
		}else {
			this.sendMessage(e.getOwnerId(), content.getOwnerId(), "您的文章有新的评论: " + content.getTitle(), MessageType.CONTENT_COMMENT_MSG);
			countUtils.inc(param.getContentId(), (long) InteractionEntityType.CONTENT, InteractionType.COMMENT);
		}
		
		String key = this.contentDataService.updateContent(e.getContentKey(), param.getContent(), ContentType.NORMAL);
		e.setContentKey(key);
		e.setFavorite(0L);
		e.setCollect(0L);
		e.setShare(0L);
		e.setComment(0L);

		e = repository.save(e);

		CommentVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);
		
		return vo;
	}

	@Override
	public Page<CommentVO> queryPage(CommentQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<Comment> example = buildSpec(param);

		Page<Comment> page = repository.findAll(example, request);
		List<CommentVO> retList = new ArrayList<>();

		for(Comment e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<CommentVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<Comment> buildSpec(CommentQueryParam param) throws CustomException {
		return new Specification<Comment>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Comment> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addEqual("ownerId", param.getOwnerId());
				builder.addEqual("contentId", param.getContentId());
				builder.addEqual("parentId", param.getParentId());
				builder.addEqual("parentOwnerId", param.getParentOwnerId());
				builder.addEqual("contentKey", param.getContentKey());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private CommentVO toVO(Comment e) throws CustomException {
		CommentVO vo = new CommentVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setOwnerId(e.getOwnerId());
		vo.setOwner(userService.getUser(e.getOwnerId()));
		vo.setContentId(e.getContentId());
		vo.setParentId(e.getParentId());
		vo.setParentOwnerId(e.getParentOwnerId());
		vo.setContent(contentDataService.findByKey(e.getContentKey()));
		vo.setParentOwner(userService.getUser(e.getParentOwnerId()));

		vo.setLike(e.getFavorite());
		vo.setCollect(e.getCollect());
		vo.setShare(e.getShare());
		vo.setComment(e.getComment());
		
		vo.setCollected(this.collectService.isCollect(InteractionEntityType.COMMENT, e.getId()));
		vo.setLiked(this.likeService.isLike(InteractionEntityType.COMMENT, e.getId()));
		vo.setCommented(this.commentService.isComment(InteractionEntityType.COMMENT, e.getId()));

		return vo;
	}
	
	@Override
	public Boolean isComment(int typeId, Long entityId) {
		Comment probe = new Comment();
		probe.setIsDelete(false);
		probe.setContentId(entityId);
		
		return this.repository.exists(Example.of(probe));
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		Comment c = this.getEntity(id);
		objectCaches.delete(id, CommentVO.class);
		super.softDelete(id);
		
		if(c.getParentId() != null) {
			countUtils.dec(c.getParentId(), (long) InteractionEntityType.COMMENT, InteractionType.COMMENT);
		}else {
			countUtils.dec(c.getContentId(), (long) InteractionEntityType.CONTENT, InteractionType.COMMENT);
		}
	}
	
	private Comment getEntity(Long id) {
		Comment ret = this.objectCaches.get(id, Comment.class);
		if(null == ret){
			Optional<Comment> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}


}
