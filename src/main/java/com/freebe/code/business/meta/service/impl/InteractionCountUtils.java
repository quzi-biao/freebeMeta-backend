package com.freebe.code.business.meta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freebe.code.business.base.entity.InteractionEntity;
import com.freebe.code.business.base.repository.BaseRepository;
import com.freebe.code.business.meta.entity.Comment;
import com.freebe.code.business.meta.entity.Content;
import com.freebe.code.business.meta.entity.Job;
import com.freebe.code.business.meta.entity.MarketProvide;
import com.freebe.code.business.meta.repository.CommentRepository;
import com.freebe.code.business.meta.repository.ContentRepository;
import com.freebe.code.business.meta.repository.JobRepository;
import com.freebe.code.business.meta.repository.MarketProvideRepository;
import com.freebe.code.business.meta.type.InteractionEntityType;
import com.freebe.code.business.meta.type.InteractionType;
import com.freebe.code.common.ObjectCaches;

/**
 * 交互计数的工具类
 * @author zhengbiaoxie
 *
 */
@Component
public class InteractionCountUtils {
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private ContentRepository contentRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private MarketProvideRepository marketProvideRepository;
	
	@Autowired
	private ObjectCaches caches;
	
	/**
	 * 减少数量
	 * @param id
	 * @param entityType
	 * @param interactionType
	 */
	public void dec(Long id, Long entityType, int interactionType) {
		BaseRepository<? extends InteractionEntity> rep = this.getRepository(entityType);
		if(null == rep) {
			return;
		}
		
		InteractionEntity e = rep.getById(id);
		if(null == e) {
			return;
		}
		
		if(interactionType == InteractionType.COLLECT) {
			if(null == e.getCollect() || e.getCollect() <= 0) {
				return;
			}
			e.setCollect(e.getCollect() - 1);
		}else if(interactionType == InteractionType.COMMENT) {
			if(null == e.getComment() || e.getCollect() <= 0) {
				return;
			}
			e.setComment(e.getComment() - 1);
		}else if(interactionType == InteractionType.LIKE) {
			if(null == e.getFavorite() || e.getCollect() <= 0) {
				return;
			}
			e.setFavorite(e.getFavorite() - 1);
		}else if(interactionType == InteractionType.SHARE) {
			if(null == e.getShare() || e.getCollect() <= 0) {
				return;
			}
			e.setShare(e.getShare() - 1);
		}
		this.updateEntity(entityType, e);
		caches.put(e.getId(), e);
	}
	
	/**
	 * 增加数量
	 * @param id
	 * @param entityType
	 * @param interactionType
	 */
	public void inc(Long id, Long entityType, int interactionType) {
		BaseRepository<? extends InteractionEntity> rep = this.getRepository(entityType);
		if(null == rep) {
			return;
		}
		
		InteractionEntity e = rep.getById(id);
		if(null == e) {
			return;
		}
		
		if(interactionType == InteractionType.COLLECT) {
			if(null == e.getCollect()) {
				e.setCollect(0L);
			}
			e.setCollect(e.getCollect() + 1);
			
		}else if(interactionType == InteractionType.COMMENT) {
			if(null == e.getComment()) {
				e.setComment(0L);
			}
			e.setComment(e.getComment() + 1);
		}else if(interactionType == InteractionType.LIKE) {
			if(null == e.getFavorite()) {
				e.setFavorite(0L);
			}
			e.setFavorite(e.getFavorite() + 1);
		}else if(interactionType == InteractionType.SHARE) {
			if(null == e.getShare()) {
				e.setShare(0L);
			}
			e.setShare(e.getShare() + 1);
		}
		this.updateEntity(entityType, e);
		caches.put(e.getId(), e);
	}
	
	private void updateEntity(long entityType, InteractionEntity e) {
		if(entityType == InteractionEntityType.JOB) {
			jobRepository.save((Job)e);
		}else if(entityType == InteractionEntityType.COMMENT) {
			commentRepository.save((Comment)e);
		}else if(entityType == InteractionEntityType.CONTENT) {
			contentRepository.save((Content)e);
		}else if(entityType == InteractionEntityType.PROVIDE) {
			marketProvideRepository.save((MarketProvide)e);
		}
	}
	
	private BaseRepository<? extends InteractionEntity> getRepository(long entityType) {
		if(entityType == InteractionEntityType.JOB) {
			return jobRepository;
		}else if(entityType == InteractionEntityType.COMMENT) {
			return commentRepository;
		}else if(entityType == InteractionEntityType.CONTENT) {
			return contentRepository;
		}else if(entityType == InteractionEntityType.PROVIDE) {
			return marketProvideRepository;
		}
		return null;
	}
}
