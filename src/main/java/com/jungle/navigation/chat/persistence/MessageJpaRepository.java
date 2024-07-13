package com.jungle.navigation.chat.persistence;

import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.persistence.entity.Message;
import com.jungle.navigation.common.exception.BusinessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageJpaRepository extends JpaRepository<Message, Long>, MessageRepository {

	Slice<Message> findByRoomIdOrderBySendTimeAsc(Long roomId, Pageable pageable);

	default Message getById(Long id) {
		return findById(id).orElseThrow(() -> new BusinessException("존재하지 않는 메시지입니다."));
	}
}
