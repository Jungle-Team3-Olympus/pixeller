package com.jungle.navigation.chat.application;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.application.repository.MemberDataAdaptor;
import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.Message;
import com.jungle.navigation.chat.presentation.dto.request.SendMessageRequest;
import com.jungle.navigation.chat.presentation.dto.response.MessageResponse;
import com.jungle.navigation.chat.presentation.dto.response.ReadMessageResponse;
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
	private final MemberDataAdaptor memberDataAdaptor;

	@Transactional
	public MessageResponse createPublicMessage(
			Long memberId, String memberName, Long roomId, SendMessageRequest request) {
		saveMessage(roomId, request, memberId);

		return MessageResponse.of(PUBLIC_ROOM, memberName, request.content());
	}

	@Transactional
	public MessageResponse createDirectMessage(
			Long senderId, String senderName, Long roomId, SendMessageRequest request) {
		validateExistChatRoom(roomId);
		saveMessage(roomId, request, senderId);

		return MessageResponse.of(roomId, senderName, request.content());
	}

	@Transactional
	public ReadMessageResponse readMessage(Long readerId, Long messageId) {
		Message message = messageRepository.getById(messageId);
		message.readMessage(readerId);

		return new ReadMessageResponse(message.getRoomId(), messageId, message.getReadCount());
	}

	private void saveMessage(Long roomId, SendMessageRequest request, Long memberId) {
		ChatRoom chatRoom = chatRoomRepository.getById(roomId);
		Message message = toEntity(request, chatRoom.getId(), memberId);
		messageRepository.save(message);
	}

	private Message toEntity(SendMessageRequest request, Long roomId, Long memberId) {
		return Message.builder().roomId(roomId).senderId(memberId).content(request.content()).build();
	}

	private void validateExistChatRoom(Long roomId) {
		chatRoomRepository.validateById(roomId);
	}
}
