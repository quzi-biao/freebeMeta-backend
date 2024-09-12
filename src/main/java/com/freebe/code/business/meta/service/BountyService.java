package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.BountyAuditParam;
import com.freebe.code.business.meta.controller.param.BountyParam;
import com.freebe.code.business.meta.controller.param.BountyQueryParam;
import com.freebe.code.business.meta.entity.Bounty;
import com.freebe.code.business.meta.vo.BountyVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface BountyService extends BaseService<Bounty> {

	BountyVO findById(Long id) throws CustomException;

	BountyVO createOrUpdate(BountyParam param) throws CustomException;

	Page<BountyVO> queryPage(BountyQueryParam param) throws CustomException;

	/**
	 * 更新悬赏的认领信息
	 * @param takeId
	 * @throws CustomException 
	 */
	void updateTake(Long bountyId, Long takeId) throws CustomException;

	/**
	 * 取消悬赏
	 * @param bountyId
	 * @return
	 * @throws CustomException
	 */
	BountyVO cancelBounty(Long bountyId) throws CustomException;
	
	/**
	 * 取消悬赏
	 * @param bountyId
	 * @return
	 * @throws CustomException
	 */
	BountyVO auditBounty(BountyAuditParam param) throws CustomException;

	/**
	 * 悬赏完成
	 * @param id
	 * @throws CustomException 
	 */
	void doneBounty(Long id) throws CustomException;

	/**
	 * 悬赏可重新认领
	 * @param id
	 * @throws CustomException 
	 */
	void restartBounty(Long id) throws CustomException;

}
