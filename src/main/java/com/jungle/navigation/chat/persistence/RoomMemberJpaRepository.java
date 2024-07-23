package com.jungle.navigation.chat.persistence;

import com.jungle.navigation.chat.application.repository.RoomMemberRepository;
import com.jungle.navigation.chat.persistence.entity.RoomMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMemberJpaRepository
		extends JpaRepository<RoomMember, Long>, RoomMemberRepository {

	List<RoomMember> findByChatRoomIdIn(List<Long> chatRoomIds);

	List<RoomMember> findAllByChatRoomId(Long chatRoomId);
}
