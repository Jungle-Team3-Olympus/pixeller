package com.jungle.navigation.chat.persistence;

import com.jungle.navigation.chat.application.repository.RoomMemberRepository;
import com.jungle.navigation.chat.persistence.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMemberJpaRepository
		extends JpaRepository<RoomMember, Long>, RoomMemberRepository {}
