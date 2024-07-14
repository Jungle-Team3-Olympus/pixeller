package com.jungle.navigation.chat.persistence;

import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.persistence.entity.Message;
import com.jungle.navigation.common.exception.BusinessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageJpaRepository extends JpaRepository<Message, Long>, MessageRepository {
	default Message getById(Long id) {
		return findById(id).orElseThrow(() -> new BusinessException("존재하지 않는 메시지입니다."));
	}

	@Query(
			"SELECT m FROM Message m "
					+ "JOIN RoomMember rm ON m.roomId = rm.chatRoomId "
					+ "WHERE rm.memberId = :memberId AND m.sendTime = (SELECT MAX(m2.sendTime) FROM Message m2 WHERE m2.roomId = m.roomId) "
					+ "ORDER BY m.sendTime DESC")
	Slice<Message> findRoomsByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
