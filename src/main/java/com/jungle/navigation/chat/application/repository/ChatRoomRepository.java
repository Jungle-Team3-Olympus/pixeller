package com.jungle.navigation.chat.application.repository;

import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.RoomType;

public interface ChatRoomRepository {

	ChatRoom getById(Long id);

	ChatRoom save(ChatRoom room);

	ChatRoom findCommonChatRoom(Long senderId, Long receiverId, RoomType roomType);

	void validateById(Long id);

	Long getLock(String key, int timeoutSeconds);

	void releaseLock(String key);
}
