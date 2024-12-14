package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.freebe.code.business.base.entity.User;
import com.freebe.code.business.base.repository.UserRepository;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.TransactionParam;
import com.freebe.code.business.meta.controller.param.TransactionQueryParam;
import com.freebe.code.business.meta.controller.param.WithdrawParam;
import com.freebe.code.business.meta.entity.Transaction;
import com.freebe.code.business.meta.repository.TransactionRepository;
import com.freebe.code.business.meta.service.ProjectMemberService;
import com.freebe.code.business.meta.service.ProjectRecordService;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.service.WalletService;
import com.freebe.code.business.meta.type.Currency;
import com.freebe.code.business.meta.type.TransactionState;
import com.freebe.code.business.meta.type.TransactionType;
import com.freebe.code.business.meta.vo.ProjectReward;
import com.freebe.code.business.meta.vo.ProjectReward.RewardItem;
import com.freebe.code.business.meta.vo.TransactionVO;
import com.freebe.code.business.meta.vo.WalletVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Slf4j
@Service
public class TransactionServiceImpl extends BaseServiceImpl<Transaction> implements TransactionService {
	// 提现账户 ID
	public final static Long WITHDRAW_WALLET_ID = 141L;
	
	// Meta 账户
	public final static Long META_WALLET_ID = 126L;
	
	@Autowired
	private TransactionRepository repository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private ProjectMemberService projectMemberService;
	
	@Autowired
	private ProjectRecordService projectRecordService;

	@Override
	public TransactionVO findById(Long id) throws CustomException {
		if(null == id) {
			return null;
		}
		Transaction ret = this.objectCaches.get(id, Transaction.class);
		if(null == ret){
			Optional<Transaction> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return toVO(ret);
	}
	
	@Transactional
	@Override
	public synchronized TransactionVO transactionAudit(Long transactionId) throws CustomException {
		checkFinanceOfficer();
		
		Transaction transaction = this.getById(transactionId);
		if(null == transaction) {
			throw new CustomException("交易不存在");
		}
		
		int state = transaction.getState().intValue();
		if(state != TransactionState.WAIT_AUDIT) {
			throw new CustomException("交易已审核，不可重复操作");
		}
		
		// 检查提现余额
		if(transaction.getTransactionType().intValue() == TransactionType.WITHDRAW) {
			WalletVO withDraw = this.walletService.findById(WITHDRAW_WALLET_ID);
			if(withDraw.getCny() < numbericCurrency(transaction.getAmount())) {
				throw new CustomException("提现账户余额不足");
			}
		}
		
		transaction.setState(TransactionState.PUBLICITY);
		transaction.setPublicStartTime(System.currentTimeMillis());
		
		transaction = this.repository.save(transaction);
		
		objectCaches.put(transaction.getId(), transaction);
		
		if(null != transaction.getProjectId()) {
			StringBuffer buf = new StringBuffer();
			WalletVO w = this.walletService.findById(transaction.getDstWalletId());
			buf.append(w.getUser().getName());
			buf.append("的结算申请已审核通过");
			projectRecordService.addRecord(transaction.getProjectId(), buf.toString());
		}
		
		return toVO(transaction);
	}
	

	@Override
	public TransactionVO reback(Long transactionId) throws CustomException {
		checkFinanceOfficer();
		
		Transaction transaction = this.getById(transactionId);
		if(null == transaction) {
			throw new CustomException("交易不存在");
		}
		
		int state = transaction.getState().intValue();
		if(!(state == TransactionState.WAIT_AUDIT || state == TransactionState.PUBLICITY)) {
			throw new CustomException("交易已结束，不可撤销");
		}
		
		transaction.setState(TransactionState.CANCEL);
		transaction.setConfirmTime(System.currentTimeMillis());
		transaction = this.repository.save(transaction);
		
		objectCaches.put(transaction.getId(), transaction);
		return toVO(transaction);
	}

	@Override
	public TransactionVO createOrUpdate(TransactionParam param) throws CustomException {
		 //接口只有财务官或项目所有者可以调用
		 checkFinanceOfficer();
		param.setTransactionType(TransactionType.FREEBE_DISTRIBUTE);
		
		return this.innerCreateOrUpdate(param);
	}
	
	@Override
	public TransactionVO withdraw(WithdrawParam param) throws CustomException {
		WalletVO wallet = this.walletService.findByUser(getCurrentUser().getId());
		if(null == wallet) {
			throw new CustomException("请求异常");
		}
		
		WalletVO withDraw = this.walletService.findById(WITHDRAW_WALLET_ID);
		if(withDraw.getCny() < param.getAmout()) {
			throw new CustomException("提现账户余额不足");
		}
		
		TransactionParam t = new TransactionParam();
		t.setAmount(param.getAmout().doubleValue());
		t.setCurrency(Currency.CNY);
		t.setDstWalletId(META_WALLET_ID); // 提现将金额返还给 META
		t.setMark("提现");
		t.setSrcWalletId(wallet.getId());		
		t.setTransactionType(TransactionType.WITHDRAW);
		
		return this.innerCreateOrUpdate(t);
	}

	@Transactional
	@Override
	public synchronized TransactionVO innerCreateOrUpdate(TransactionParam param) throws CustomException {
		if(null == param.getDstWalletId() || null == param.getSrcWalletId()) {
			throw new CustomException("钱包地址为空");
		}
		
		if(null == param.getAmount() || null == param.getCurrency() || null == param.getTransactionType()) {
			throw new CustomException("参数错误");
		}
		
		Transaction e = this.getUpdateEntity(param, false);
		if(e.getState() != null && 
				(e.getState().intValue() == TransactionState.CONFIRM || e.getState().intValue() == TransactionState.CONFIRMING)) {
			throw new CustomException("交易已确认，不可修改");
		}
		
		WalletVO wallet = this.walletService.findById(param.getSrcWalletId());
		if(param.getCurrency() == Currency.CNY) {
			if(wallet.getCny() < param.getAmount()) {
				throw new CustomException("Y余额不足");
			}
		}else {
			if(wallet.getFreeBe() < param.getAmount()) {
				throw new CustomException("余额不足");
			}
		}
		
		if(null == param.getId()) {
			e.setSrcWalletId(param.getSrcWalletId());
			e.setDstWalletId(param.getDstWalletId());
			e.setTransactionType(param.getTransactionType());
			e.setState(TransactionState.WAIT_AUDIT);
			e.setProjectId(param.getProjectId());
			e.setCurrency(param.getCurrency());
		}
		
		e.setAmount(currencyStr(param.getAmount()));
		e.setMark(param.getMark());

		e = repository.save(e);
		
		// 悬赏奖励直接发放
//		if(TransactionType.FREEBE_DISTRIBUTE != param.getTransactionType()) {
//			this.confirm(e.getId());
//		}

		TransactionVO vo = toVO(e);
		objectCaches.put(e.getId(), e);

		return vo;
	}

	@Override
	public Page<TransactionVO> queryPage(TransactionQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);
		if(!StringUtils.isEmpty(param.getUserName())) {
			param.setWalletIds(this.queryWalletIdByUserName(param.getUserName()));
			if(null == param.getWalletIds() || param.getWalletIds().size() == 0) {
				return new PageImpl<TransactionVO>(new ArrayList<>(), request, 0);
			}
		}

		Specification<Transaction> example = buildSpec(param);

		Page<Transaction> page = repository.findAll(example, request);
		List<TransactionVO> retList = new ArrayList<>();

		for(Transaction e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<TransactionVO>(retList, page.getPageable(), page.getTotalElements());
	}
	
	@Transactional
	@Override
	public synchronized TransactionVO confirm(Long transactionId) throws CustomException {
		Transaction transaction = this.getById(transactionId);
		if(null == transaction) {
			throw new CustomException("交易不存在");
		}
				
		transaction.setState(TransactionState.CONFIRMING);
		this.repository.save(transaction);
		
		//执行转账动作
		WalletVO wallet = this.walletService.findById(transaction.getSrcWalletId());
		
		try {
			// 提现交易，还需要从提现账户中移除对应的金额
			if(transaction.getTransactionType().intValue() == TransactionType.WITHDRAW) {
				WalletVO withDraw = this.walletService.findById(WITHDRAW_WALLET_ID);
				if(withDraw.getCny() < numbericCurrency(transaction.getAmount())) {
					throw new CustomException("提现账户余额不足");
				}
			}
			
			Double amount = numbericCurrency(transaction.getAmount());
			if(transaction.getCurrency() == Currency.CNY) {
				if(wallet.getCny() < amount) {
					throw new CustomException("余额不足");
				}
			}else {
				if(wallet.getFreeBe() < amount) {
					throw new CustomException("余额不足");
				}
			}
			
			if(transaction.getCurrency() == Currency.CNY) {
				this.walletService.transferCny(transactionId, transaction.getSrcWalletId(), transaction.getDstWalletId(), amount);
			}else {
				this.walletService.transferFreeBe(transactionId, transaction.getSrcWalletId(), transaction.getDstWalletId(), amount);
			}
			
			transaction.setState(TransactionState.CONFIRM);
			transaction.setConfirmTime(System.currentTimeMillis());
			
			// 提现交易，还需要从提现账户中移除对应的金额
			if(transaction.getTransactionType().intValue() == TransactionType.WITHDRAW) {
				this.walletService.burn(WITHDRAW_WALLET_ID, transaction.getAmount(), transaction.getCurrency());
			}
			
		} catch (CustomException e) {
			transaction.setState(TransactionState.FAILED);
			transaction.setFailedReason(e.getMessage());
			log.error(e.getMessage(), e);
		}
		
		transaction = this.repository.save(transaction);
		objectCaches.put(transaction.getId(), transaction);
		
		if(transaction.getTransactionType() == TransactionType.FREEBE_DISTRIBUTE) {
			projectMemberService.billEnd(transactionId, transaction.getState());
		}
		
		return toVO(transaction);
	}
	
	@Override
	public ProjectReward getProjectReward(Long projectId) throws CustomException {
		List<Object[]> records = this.repository.getProjectReward(projectId);
		
		if(null != records && records.size() > 0) {
			ProjectReward r = new ProjectReward();
			r.setTotalAmount(0D);
			r.setRewards(new ArrayList<>());
			
			for(Object[] record : records) {
				Long dstId = Long.parseLong(String.valueOf(record[0]));
				Double amount = Double.parseDouble(String.valueOf(record[1]));
				
				RewardItem item = new RewardItem();
				item.setAmount(numbericCurrency(amount.longValue()));
				item.setWallet(this.walletService.findById(dstId));
				r.setTotalAmount(r.getTotalAmount() + item.getAmount());
				
				r.getRewards().add(item);
			}
			
			return r;
		}
		
		return null;
	}

	private Specification<Transaction> buildSpec(TransactionQueryParam param) throws CustomException {
		return new Specification<Transaction>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Transaction> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				if(null != param.getUserId()) {
					try {
						WalletVO wallet = walletService.findByUser(param.getUserId());
						Long walletId = wallet.getId();
						Predicate predicateOr = criteriaBuilder.or(
									criteriaBuilder.equal(root.get("dstWalletId"), walletId),
									criteriaBuilder.equal(root.get("srcWalletId"), walletId)
								);
						builder.addEqual("projectId", param.getProjectId());
						builder.addEqual("state", param.getState());
						builder.addEqual("transactionType", param.getTransactionType());

						builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
						return query.where(builder.getPredicate(), predicateOr).getRestriction();
					} catch (CustomException e) {
						log.error(e.getMessage(), e);
					}
				}
				
				if(null == param.getWalletIds()) {
					builder.addEqual("dstWalletId", param.getDstWalletId());
					builder.addEqual("srcWalletId", param.getSrcWalletId());
				}else {
					builder.addIn("dstWalletId", param.getWalletIds());
				}
				
				builder.addEqual("projectId", param.getProjectId());
				builder.addEqual("state", param.getState());
				builder.addEqual("transactionType", param.getTransactionType());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private TransactionVO toVO(Transaction e) throws CustomException {
		TransactionVO vo = new TransactionVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setSrcWallet(this.walletService.findById(e.getSrcWalletId()));
		vo.setDstWallet(this.walletService.findById(e.getDstWalletId()));
		vo.setAmount(numbericCurrency(e.getAmount()));
		vo.setCurrency(e.getCurrency());
		vo.setConfirmTime(e.getConfirmTime());
		vo.setHash(e.getHash());
		vo.setTransactionType(e.getTransactionType());
		vo.setState(e.getState());
		vo.setMark(e.getMark());
		vo.setProjectId(e.getProjectId());
		vo.setPublicStartTime(e.getPublicStartTime());		
		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, Transaction.class);
		super.softDelete(id);
	}
	
	private void checkFinanceOfficer() throws CustomException {
		this.checkPermssion(ROLE_CF_CODE);
	}
	
	private List<Long> queryWalletIdByUserName(String userName) throws CustomException {
		User user = new User();
		user.setName(userName);	
		user.setIsDelete(false);
		
		List<User> userList = this.userRepository.findAll(Example.of(user));
		if(null == userList || userList.size() == 0) {
			return null;
		}
		
		List<Long> ret = new ArrayList<>();
		for(User u : userList) {
			WalletVO w = this.walletService.findByUser(u.getId());
			if(null != w) {
				ret.add(w.getId());
			}
		}
		
		return ret;
	}
}
