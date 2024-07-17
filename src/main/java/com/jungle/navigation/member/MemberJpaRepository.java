package com.jungle.navigation.member;

import com.jungle.navigation.common.exception.BusinessException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Integer> {
	List<MemberEntity> findAllByIdIn(List<Integer> memberIds);

	default MemberEntity getById(Integer memberId) {
		return findById(memberId).orElseThrow(() -> new BusinessException("존재하지 않는 멤버입니다."));
	}
}
