package com.jungle.navigation.chat.application;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.RoomType;
import com.jungle.navigation.chat.presentation.dto.request.CreateDirectRoomRequest;
import com.jungle.navigation.chat.presentation.dto.response.CreateRoomResponse;
import com.jungle.navigation.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public CreateRoomResponse createDirectRoom(Long senderId, CreateDirectRoomRequest request) {
		ChatRoom commonChatRoom =
				chatRoomRepository.findCommonChatRoom(senderId, request.receiverId(), RoomType.DIRECT);

		if (commonChatRoom == null) {
			throw new BusinessException("이미 두 유저간의 채팅방이 존재합니다.");
		}

		ChatRoom chatRoom = chatRoomRepository.save(toEntity());
		return CreateRoomResponse.of(chatRoom.getId());
	}

	private ChatRoom toEntity() {
		return ChatRoom.of(RoomType.DIRECT);
	}
}
