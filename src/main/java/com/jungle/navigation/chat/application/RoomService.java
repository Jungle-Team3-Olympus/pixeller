package com.jungle.navigation.chat.application;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.RoomType;
import com.jungle.navigation.chat.presentation.dto.request.CreateDirectRoomRequest;
import com.jungle.navigation.chat.presentation.dto.response.CreateRoomResponse;
import com.jungle.navigation.chat.presentation.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {
	private static final String sender = "관리자";
	private static final String WELCOME_MESSAGE = " 님이 입장하였습니다.";

	private final ChatRoomRepository chatRoomRepository;

	public MessageResponse joinRoom(String memberName) {

		return MessageResponse.of(1L, sender, memberName + WELCOME_MESSAGE);
	}

	@Transactional
	public CreateRoomResponse createDirectRoom(Long senderId, CreateDirectRoomRequest request) {
		ChatRoom commonChatRoom =
				chatRoomRepository.findCommonChatRoom(senderId, request.receiverId(), RoomType.DIRECT);

		if (commonChatRoom == null) {
			throw new IllegalArgumentException("이미 두 유저간의 채팅방이 존재합니다.");
		}

		ChatRoom chatRoom = chatRoomRepository.save(toEntity());
		return CreateRoomResponse.of(chatRoom.getId());
	}

	private ChatRoom toEntity() {
		return ChatRoom.of(RoomType.DIRECT);
	}
}
