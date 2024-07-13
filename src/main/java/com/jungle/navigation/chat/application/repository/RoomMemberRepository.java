package com.jungle.navigation.chat.application.repository;

import com.jungle.navigation.chat.persistence.entity.RoomMember;

public interface RoomMemberRepository {
	RoomMember save(RoomMember roomMember);
}
