package com.jungle.navigation.chat.application;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.RoomType;
import com.jungle.navigation.chat.presentation.dto.response.RoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacadeRoomService {
	private final RoomService roomService;
	private final ChatRoomRepository chatRoomRepository;

	public RoomResponse createDirectRoom(Long senderId, Long oppositeId) {
		Long chatRoomId = 0L;

		ChatRoom commonChatRoom =
				chatRoomRepository.findCommonChatRoom(senderId, oppositeId, RoomType.DIRECT);
		if (commonChatRoom != null) {
			return RoomResponse.of(commonChatRoom.getId());
		}

		String lockName = getNamedLockName(senderId, oppositeId);
		try {
			chatRoomRepository.getLock(lockName, 60);
			ChatRoom exist = chatRoomRepository.findCommonChatRoom(senderId, oppositeId, RoomType.DIRECT);
			if (exist != null) {
				return RoomResponse.of(exist.getId());
			}
			chatRoomId = roomService.createDirectRoom(senderId, oppositeId);
		} finally {
			chatRoomRepository.releaseLock(lockName);
		}
		return RoomResponse.of(chatRoomId);
	}

	private String getNamedLockName(Long senderId, Long oppositeId) {
		return "direct_room_lock_"
				+ Math.min(senderId, oppositeId)
				+ "_"
				+ Math.max(senderId, oppositeId);
	}
}
