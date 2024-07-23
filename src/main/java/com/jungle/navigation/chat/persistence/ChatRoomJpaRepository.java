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
		if (existsById(id)) {
			return;
		}
		throw new BusinessException("존재하지 않는 채팅방입니다.");
	}

	@Query(
			"SELECT r "
					+ "FROM ChatRoom r "
					+ "JOIN RoomMember rm1 ON r.id = rm1.chatRoomId "
					+ "JOIN RoomMember rm2 ON r.id = rm2.chatRoomId "
					+ "WHERE r.roomType = :roomType "
					+ "AND rm1.memberId = :senderId "
					+ "AND rm2.memberId = :receiverId "
					+ "AND rm1.memberId <> rm2.memberId")
	ChatRoom findCommonChatRoom(
			@Param("senderId") Long senderId,
			@Param("receiverId") Long receiverId,
			@Param("roomType") RoomType roomType);

	@Query(value = "select GET_LOCK(:key, :timeoutSeconds)", nativeQuery = true)
	Long getLock(String key, int timeoutSeconds);

	@Query(value = "select RELEASE_LOCK(:key)", nativeQuery = true)
	void releaseLock(String key);
}
