package com.freebe.code.business.meta.service.impl;


import java.math.BigDecimal;
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

import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.WalletParam;
import com.freebe.code.business.meta.controller.param.WalletQueryParam;
import com.freebe.code.business.meta.entity.Wallet;
import com.freebe.code.business.meta.repository.WalletRepository;
import com.freebe.code.business.meta.service.WalletService;
import com.freebe.code.business.meta.vo.FinanceInfo;
import com.freebe.code.business.meta.vo.WalletVO;
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
public class WalletServiceImpl extends BaseServiceImpl<Wallet> implements WalletService {
	@Autowired
	private WalletRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private UserService userService;

	@Override
	public WalletVO findById(Long id) throws CustomException {
		Wallet ret = this.objectCaches.get(id, Wallet.class);
		if(null == ret){
			Optional<Wallet> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		
		updateCache(ret);
		
		return toVO(ret);
	}
	

	@Override
	public WalletVO findByAddress(String address) throws CustomException {
		String id = "address: " + address;
		
		Wallet ret = this.objectCaches.get(id, Wallet.class);
		if(null != ret) {
			return toVO(ret);
		}
		
		Wallet wallet = new Wallet();
		wallet.setAddress(address);
		wallet.setIsDelete(false);
		
		List<Wallet> wallets = this.repository.findAll(Example.of(wallet));
		
		if(null == wallets || wallets.size() == 0) {
			return null;
		}
		
		ret = wallets.get(0);
		updateCache(ret);
		
		return toVO(ret);
	}

	@Override
	public WalletVO findByUser(Long userId) throws CustomException {
		String id = "u: " + userId;
		
		Wallet ret = this.objectCaches.get(id, Wallet.class);
		if(null != ret) {
			return toVO(ret);
		}
		
		Wallet wallet = new Wallet();
		wallet.setUserId(userId);
		wallet.setIsDelete(false);
		
		List<Wallet> wallets = this.repository.findAll(Example.of(wallet));
		
		if(null == wallets || wallets.size() == 0) {
			WalletParam param = new WalletParam();
			param.setUserId(userId);
			this.createOrUpdate(param);
			wallets = this.repository.findAll(Example.of(wallet));
			if(null == wallets || wallets.size() == 0) {
				return null;
			}
		}
		
		ret = wallets.get(0);
		updateCache(ret);
		
		return toVO(ret);
	}

	@Override
	public WalletVO createOrUpdate(WalletParam param) throws CustomException {
		Wallet e = this.getUpdateEntity(param, false);

		e.setUserId(param.getUserId());
		e.setAddress(param.getAddress());
		e.setFreeBe(0L);
		e.setUsdt("0");
		e.setCny("0");

		e = repository.save(e);

		WalletVO vo = toVO(e);
		updateCache(e);

		return vo;
	}

	@Override
	public Page<WalletVO> queryPage(WalletQueryParam param) throws CustomException {
		PageRequest request = PageUtils.toPageRequest(param);
		
		Specification<Wallet> example = buildSpec(param);

		Page<Wallet> page = repository.findAll(example, request);
		List<WalletVO> retList = new ArrayList<>();

		for(Wallet e:  page.getContent()) {
			if(e.getUserId() <= 0) {
				continue;
			}
			retList.add(toVO(e));
		}
		return new PageImpl<WalletVO>(retList, page.getPageable(), page.getTotalElements());
	}
	
	@Override
	public FinanceInfo getFinanceInfo() throws CustomException {
		WalletVO w = this.findById(1L);
		FinanceInfo info = new FinanceInfo();
		info.setTotalFreeBe(w.getFreeBe());
		info.setTotalDistrubuteFreeBe(numbericCurrency(String.valueOf(this.repository.totalFreeBe())));
		info.setUsdtAmount(0D);
		
		return info;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, WalletVO.class);
		super.softDelete(id);
	}


	@Transactional
	@Override
	public synchronized void transferFreeBe(Long transactionId, Long src, Long dst, Double amount) throws CustomException {
		BigDecimal decAmount = new BigDecimal(currencyStr(amount));
		
		Wallet srcWallet = this.getById(src);
		BigDecimal srcAmount = new BigDecimal(srcWallet.getFreeBe());
		if(srcAmount.compareTo(decAmount) < 0) {
			throw new CustomException("交易失败，余额不足");
		}
		
		srcWallet.setFreeBe(srcAmount.subtract(decAmount).longValue());
		Wallet dstWallet = this.getById(dst);
		BigDecimal dstAmount = new BigDecimal(dstWallet.getFreeBe());
		dstWallet.setFreeBe(dstAmount.add(decAmount).longValue());
		
		this.repository.save(srcWallet);
		this.repository.save(dstWallet);
		
		updateCache(dstWallet);
		updateCache(srcWallet);
	}
	
	private void updateCache(Wallet w) {
		objectCaches.put("u:" + w.getUserId(), w);
		objectCaches.put("address: " + w.getAddress(), w);
		objectCaches.put(w.getId(), w);
	}
	
	private Specification<Wallet> buildSpec(WalletQueryParam param) throws CustomException {
		return new Specification<Wallet>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Wallet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Wallet> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private WalletVO toVO(Wallet e) throws CustomException {
		WalletVO vo = new WalletVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setUserId(e.getUserId());
		vo.setUser(userService.getUser(e.getUserId()));
		vo.setAddress(e.getAddress());
		vo.setFreeBe(numbericCurrency(e.getFreeBe()));
		vo.setUsdt(numbericCurrency(e.getUsdt()));
		vo.setCny(numbericCurrency(e.getCny()));

		return vo;
	}
}
