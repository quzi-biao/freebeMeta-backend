package com.freebe.code.business.meta.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.freebe.code.business.base.repository.BaseRepository;
import com.freebe.code.business.meta.entity.Bounty;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Repository
public interface BountyRepository extends BaseRepository<Bounty> {
	@Query(value = "select sum(reward) from t_bounty where project_id=?1 and state in (0, 1, 2, 3); ", nativeQuery = true)
	Long getProjectReward(Long projectId);
}
