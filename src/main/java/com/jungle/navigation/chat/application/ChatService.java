package com.jungle.navigation.chat.application;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.Message;
import com.jungle.navigation.chat.persistence.entity.RoomType;
import com.jungle.navigation.chat.presentation.dto.request.SendMessageRequest;
import com.jungle.navigation.chat.presentation.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
	private static final Long PUBLIC_ROOM = 1L;

	private final ChatRoomRepository chatRoomRepository;
	private final MessageRepository messageRepository;

	@Transactional
	public MessageResponse createPublicMessage(
			Long memberId, String memberName, Long roomId, SendMessageRequest request) {
		saveMessage(roomId, request, memberId);

		return MessageResponse.of(PUBLIC_ROOM, memberName, request.content());
	}

	@Transactional
	public MessageResponse createDirectMessage(
			Long senderId, String senderName, Long receiverId, SendMessageRequest request) {
		ChatRoom chatRoom = getChatRoom(senderId, receiverId, RoomType.DIRECT);
		saveMessage(chatRoom.getId(), request, senderId);

		return MessageResponse.of(chatRoom.getId(), senderName, request.content());
	}

	private void saveMessage(Long roomId, SendMessageRequest request, Long memberId) {
		ChatRoom chatRoom = chatRoomRepository.getById(roomId);
		Message message = toEntity(request, chatRoom.getId(), memberId);
		messageRepository.save(message);
	}

	private Message toEntity(SendMessageRequest request, Long roomId, Long memberId) {
		return Message.builder().roomId(roomId).senderId(memberId).content(request.content()).build();
	}

	private ChatRoom getChatRoom(Long senderId, Long receiverId, RoomType roomType) {
		ChatRoom commonChatRoom = chatRoomRepository.findCommonChatRoom(senderId, receiverId, roomType);

		if (commonChatRoom == null) {
			throw new IllegalStateException("상대방과의 채팅방이 존재하지 않습니다.");
		}

		return commonChatRoom;
	}
}
