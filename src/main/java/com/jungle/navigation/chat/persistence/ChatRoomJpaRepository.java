package com.jungle.navigation.chat.persistence;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.RoomType;
import com.jungle.navigation.common.exception.BusinessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepository {

	default ChatRoom getById(Long id) {
		return findById(id).orElseThrow(() -> new BusinessException("존재하지 않는 방입니다."));
	}

	default void validateById(Long id) {
		if (!existsById(id)) {
			throw new BusinessException("존재하지 않는 채팅방입니다.");
		}
	}

	@Query(
			"SELECT DISTINCT r "
					+ "FROM RoomMember rm "
					+ "JOIN ChatRoom r ON rm.chatRoomId = r.id "
					+ "WHERE r.roomType = :roomType "
					+ "AND (rm.memberId = :senderId OR rm.memberId = :receiverId) "
					+ "GROUP BY r.id "
					+ "HAVING COUNT(DISTINCT rm.memberId) = 2")
	ChatRoom findCommonChatRoom(
			@Param("senderId") Long senderId,
			@Param("receiverId") Long receiverId,
			@Param("roomType") RoomType roomType);
}
