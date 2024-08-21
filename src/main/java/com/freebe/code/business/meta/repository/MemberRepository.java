package com.freebe.code.business.meta.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.freebe.code.business.meta.entity.Member;
import com.freebe.code.business.base.repository.BaseRepository;

@Repository
public interface MemberRepository extends BaseRepository<Member> {
	@Query(value = "select max(id) from t_member; ", nativeQuery = true)
	Long getMaxId();
}
