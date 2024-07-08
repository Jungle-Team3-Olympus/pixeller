package com.jungle.navigation.auth.persistence;

import com.jungle.navigation.auth.application.exception.NotMatchMemberException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	default Member getById(Long memberId) {
		return findById(memberId).orElseThrow(NotMatchMemberException::new);
	}
}
