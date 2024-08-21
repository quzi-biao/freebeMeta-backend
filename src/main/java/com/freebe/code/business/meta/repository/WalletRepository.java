package com.freebe.code.business.meta.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.freebe.code.business.meta.entity.Wallet;
import com.freebe.code.business.base.repository.BaseRepository;

@Repository
public interface WalletRepository extends BaseRepository<Wallet> {
	
	@Query(value = "select sum(free_be) from t_wallet where id != 1; ", nativeQuery = true)
	public Long totalFreeBe();
}
