package com.jungle.navigation.chat.application;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.application.repository.MemberDataAdaptor;
import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.Message;
import com.jungle.navigation.chat.presentation.dto.request.SendMessageRequest;
import com.jungle.navigation.chat.presentation.dto.response.EachMessage;
import com.jungle.navigation.chat.presentation.dto.response.MessageResponse;
import com.jungle.navigation.chat.presentation.dto.response.MessagesResponse;
import com.jungle.navigation.chat.presentation.dto.response.ReadMessageResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

	public MessagesResponse<EachMessage> findByChatRoomId(
			Long chatRoomId, int messageId, int pageSize) {
		chatRoomRepository.validateById(chatRoomId);
		return getMessagesByRoom(chatRoomId, messageId, pageSize);
	}

	private MessagesResponse<EachMessage> getMessagesByRoom(
			Long roomId, int messageId, int pageSize) {
		Pageable pageable = PageRequest.of(messageId, pageSize);
		Slice<Message> messages = messageRepository.findByRoomIdOrderBySendTimeAsc(roomId, pageable);
		Map<Long, String> sendersName = getSenderName(messages.getContent());

		List<EachMessage> data =
				messages.stream()
						.map(
								message ->
										new EachMessage(
												sendersName.get(message.getSenderId()),
												message.getContent(),
												message.getReadCount()))
						.toList();

		return new MessagesResponse(
				roomId, data, messages.hasNext(), messages.getNumber(), messages.getSize());
	}

	private Map<Long, String> getSenderName(List<Message> messages) {
		List<Long> memberIds = messages.stream().map(Message::getSenderId).toList();

		return getMemberName(memberIds);
	}

	private Map<Long, String> getMemberName(List<Long> memberIds) {
		List<MemberData> memberData = memberDataAdaptor.memberData(memberIds);

		return memberData.stream()
				.collect(Collectors.toMap(MemberData::getMemberId, MemberData::getName));
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
