package com.jungle.navigation.chat.application;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.application.repository.MemberDataAdaptor;
import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.Message;
import com.jungle.navigation.chat.presentation.dto.request.SendMessageRequest;
import com.jungle.navigation.chat.presentation.dto.response.EachMessage;
import com.jungle.navigation.chat.presentation.dto.response.MessageResponse;
import com.jungle.navigation.chat.presentation.dto.response.ReadMessageResponse;
import com.jungle.navigation.common.presentation.respnose.SliceResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
	private final ChatRoomRepository chatRoomRepository;
	private final MessageRepository messageRepository;
	private final MemberDataAdaptor memberDataAdaptor;

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

	public SliceResponse<EachMessage> findByChatRoomId(
			Long chatRoomId, int pageNumber, int pageSize) {
		chatRoomRepository.validateById(chatRoomId);
		return getMessagesByRoom(chatRoomId, pageNumber, pageSize);
	}

	private SliceResponse<EachMessage> getMessagesByRoom(Long roomId, int pageNumber, int pageSize) {
		Slice<Message> messages = getMessagesByRoomId(roomId, pageNumber, pageSize);
		Map<Long, String> sendersName = getSenderName(messages.getContent());

		List<EachMessage> data =
				messages.stream()
						.map(
								message ->
										new EachMessage(
												message.getId(),
												sendersName.get(message.getSenderId()),
												message.getContent(),
												message.getReadCount()))
						.toList();

		return new SliceResponse<>(
				data, messages.getNumber(), messages.getSize(), messages.isFirst(), messages.isLast());
	}

	private Slice<Message> getMessagesByRoomId(Long roomId, int pageNumber, int pageSize) {
		Sort sort = Sort.by(Direction.ASC, "sendTime");
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		return messageRepository.findAllByRoomId(roomId, pageable);
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
		Message message = Message.of(chatRoom.getId(), memberId, request.content());
		messageRepository.save(message);
	}

	private void validateExistChatRoom(Long roomId) {
		chatRoomRepository.validateById(roomId);
	}
}
