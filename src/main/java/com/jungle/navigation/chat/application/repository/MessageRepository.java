package com.jungle.navigation.chat.application.repository;

import com.jungle.navigation.chat.persistence.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageRepository {
	Slice<Message> findByRoomIdOrderBySendTimeAsc(Long memberId, Pageable pageable);

	Message save(final Message message);

	Message getById(Long id);

	Slice<Message> findRoomsByMemberId(Long memberId, Pageable pageable);
}
