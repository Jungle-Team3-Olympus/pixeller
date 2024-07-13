package com.jungle.navigation.chat.application.repository;

import com.jungle.navigation.chat.application.MemberData;
import com.jungle.navigation.member.MemberEntity;
import com.jungle.navigation.member.MemberJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDataAdaptor {
	private final MemberJpaRepository memberRepository;

	public List<MemberData> memberData(List<Long> memberIds) {
		List<MemberEntity> memberEntities = memberRepository.findAllByIdIn(getMemberIds(memberIds));

		return memberEntities.stream()
				.map(member -> MemberData.of(member.getMemberId(), member.getUsername()))
				.toList();
	}

	public String getMember(Long memberId) {
		MemberEntity member = memberRepository.getById(Integer.valueOf(String.valueOf(memberId)));

		return member.getUsername();
	}

	private List<Integer> getMemberIds(List<Long> memberIds) {
		return memberIds.stream().map(Long::intValue).toList();
	}
}
