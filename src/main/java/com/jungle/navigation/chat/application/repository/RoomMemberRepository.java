package com.jungle.navigation.chat.application.repository;

import com.jungle.navigation.chat.persistence.entity.RoomMember;
import java.util.List;

public interface RoomMemberRepository {
	RoomMember save(RoomMember roomMember);

	List<RoomMember> findByChatRoomIdIn(List<Long> chatRoomIds);

	List<RoomMember> findAllByChatRoomId(Long chatRoomId);
}
