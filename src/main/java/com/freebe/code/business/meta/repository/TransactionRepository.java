package com.freebe.code.business.meta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.freebe.code.business.base.repository.BaseRepository;
import com.freebe.code.business.meta.entity.Transaction;

@Repository
public interface TransactionRepository extends BaseRepository<Transaction> {

	@Query(value = "select dst_wallet_id,sum(amount) from t_transaction where project_id=?1 and state=3 group by dst_wallet_id; ", nativeQuery = true)
	List<Object[]> getProjectReward(Long projectId);
}
