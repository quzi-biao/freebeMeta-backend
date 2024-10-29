package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.Comment;
import com.freebe.code.business.meta.vo.CommentVO;
import com.freebe.code.business.meta.controller.param.CommentParam;
import com.freebe.code.business.meta.controller.param.CommentQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface CommentService extends BaseService<Comment> {

	CommentVO findById(Long id) throws CustomException;

	CommentVO createOrUpdate(CommentParam param) throws CustomException;

	Page<CommentVO> queryPage(CommentQueryParam param) throws CustomException;

}
