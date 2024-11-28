package com.freebe.code.business.meta.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import com.freebe.code.business.meta.entity.Transaction;
import com.freebe.code.business.meta.repository.TransactionRepository;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.type.TransactionState;
import com.freebe.code.common.CommonExecutor;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.KVStorage;

import lombok.extern.slf4j.Slf4j;

/**
 * 交易定时器
 * @author zhengbiaoxie
 *
 */
@Component
@Slf4j
public class TransactionTimer implements AutoCloseable {
	private String PUBLIC_KEEP_KEY = "PUBLIC_KEEP_KEY";
	
	// 默认公示期是 48h
	private long DEFAULT_PUBLIC_KEEP = 120 * 1000;
	
	@Autowired
	private TransactionRepository repository;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private KVStorage kv;
	
	private Boolean exit = false;
	
	@PostConstruct
	public void init() {
		CommonExecutor.execute(new Runnable() {
			
			@Override
			public void run() {
				while(!exit) {
					if(exit) {
						break;
					}
					
					checkTime();
					
					// 每 1min 检查一次
					CommonExecutor.sleep(60000);
				}
			}
			
		});
	}
	
	public synchronized void checkTime() {
		Long keepTime = this.getKeepTime();
		
		Transaction probe = new Transaction();
		probe.setIsDelete(false);
		probe.setState(TransactionState.PUBLICITY);
		
		List<Transaction> transactions = this.repository.findAll(Example.of(probe));
		if(null == transactions || transactions.size() == 0) {
			return;
		}
		
		for(Transaction trans : transactions) {
			if(null == trans.getPublicStartTime()) {
				trans.setPublicStartTime(System.currentTimeMillis());
				this.repository.save(trans);
			}else {
				// 公示期结束
				if(System.currentTimeMillis() - trans.getPublicStartTime() > keepTime) {
					try {
						this.transactionService.confirm(trans.getId());
					} catch (CustomException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
		
	}
	
	private long getKeepTime() {
		String str = this.kv.get(PUBLIC_KEEP_KEY);
		if(null == str || str.length() == 0) {
			return DEFAULT_PUBLIC_KEEP;
		}
		
		try {
			Long ret = Long.parseLong(str);
			return ret;
		} catch (NumberFormatException e) {
			return DEFAULT_PUBLIC_KEEP;
		}
	}


	@Override
	public void close() throws Exception {
		this.exit = true;
	}
}
