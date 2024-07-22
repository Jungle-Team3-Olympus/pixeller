package com.jungle.navigation.chat.application;

import static com.jungle.navigation.chat.support.WebSocketEndpoints.SUB_PUBLIC_ROOM;
import static com.jungle.navigation.chat.support.WebSocketEndpoints.getDirectMessageDestination;
import static com.jungle.navigation.chat.support.WebSocketEndpoints.getSubInfoDestination;

import com.jungle.navigation.chat.application.publisher.MessagePublisher;
import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.application.repository.MemberDataAdaptor;
import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.application.repository.RoomMemberRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.Message;
import com.jungle.navigation.chat.persistence.entity.RoomMember;
import com.jungle.navigation.chat.presentation.dto.request.SendMessageRequest;
import com.jungle.navigation.chat.presentation.dto.response.EachMessage;
import com.jungle.navigation.chat.presentation.dto.response.MessageResponse;
import com.jungle.navigation.chat.presentation.dto.response.ReadMessageResponse;
import com.jungle.navigation.common.exception.BusinessException;
import com.jungle.navigation.common.presentation.respnose.SliceResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ChatService {
	private static final int DIRECT_CHAT_ROOM_OPPOSITE_MEMBER_COUNT = 1;
	private final ChatRoomRepository chatRoomRepository;
	private final MessageRepository messageRepository;
	private final RoomMemberRepository roomMemberRepository;
	private final MemberDataAdaptor memberDataAdaptor;
	private final MessagePublisher messagePublisher;

	@Transactional
	public void createDirectMessage(
			Long senderId, String senderName, Long roomId, SendMessageRequest request) {
		validateExistChatRoom(roomId);
		saveMessage(roomId, request, senderId);
		RoomMember roomMember = getDirectChatOppositeMemberId(roomId, senderId);

		MessageResponse response = MessageResponse.of(roomId, senderName, request.content());

		messagePublisher.send(getDirectMessageDestination(roomId), response);
		messagePublisher.send(getSubInfoDestination(roomMember.getMemberId()), response);
	}

	@Transactional
	public void readMessage(Long readerId, Long messageId) {
		Message message = messageRepository.getById(messageId);
		message.readMessage(readerId);

		ReadMessageResponse response =
				new ReadMessageResponse(message.getRoomId(), messageId, message.getReadCount());
		messagePublisher.send(getDirectMessageDestination(response.chatRoomId()), response);
	}

	public void findByChatRoomId(Long chatRoomId, int pageNumber, int pageSize) {
		chatRoomRepository.validateById(chatRoomId);
		SliceResponse<EachMessage> response = getMessagesByRoom(chatRoomId, pageNumber, pageSize);

		messagePublisher.send(getDirectMessageDestination(chatRoomId), response);
	}

	public void createPublicMessage(String senderName, SendMessageRequest request) {
		MessageResponse message = MessageResponse.of(1L, senderName, request.content());
		messagePublisher.send(SUB_PUBLIC_ROOM, message);
	}

	private SliceResponse<EachMessage> getMessagesByRoom(Long roomId, int pageNumber, int pageSize) {
		Slice<Message> messages = getMessagesByRoomId(roomId, pageNumber, pageSize);
		List<Message> sortedMessage =
				messages.stream().sorted(Comparator.comparing(Message::getSendTime)).toList();

		Map<Long, String> sendersName = getSenderName(messages.getContent());

		List<EachMessage> data =
				sortedMessage.stream()
						.map(
								message ->
										new EachMessage(
												message.getId(),
												sendersName.get(message.getSenderId()),
												message.getContent(),
												message.getReadCount()))
						.toList();

		return SliceResponse.of(
				data, messages.getNumber(), messages.getSize(), messages.isFirst(), messages.isLast());
	}

	private Slice<Message> getMessagesByRoomId(Long roomId, int pageNumber, int pageSize) {
		Sort sort = Sort.by(Direction.DESC, "sendTime");
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

	private RoomMember getDirectChatOppositeMemberId(Long roomId, Long senderId) {
		List<RoomMember> roomMembers = getOppositeMemberId(roomId, senderId);

		if (roomMembers.size() != DIRECT_CHAT_ROOM_OPPOSITE_MEMBER_COUNT) {
			throw new BusinessException(String.format("%s direct chat room에 2명 이상의 유저가 있습니다.", roomId));
		}

		return roomMembers.get(0);
	}

	private List<RoomMember> getOppositeMemberId(Long roomId, Long senderId) {
		return roomMemberRepository.findAllByChatRoomId(roomId).stream()
				.filter(roomMember -> roomMember.isOtherMember(senderId))
				.toList();
	}
}
