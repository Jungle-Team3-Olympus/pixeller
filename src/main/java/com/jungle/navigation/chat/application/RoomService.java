package com.jungle.navigation.chat.application;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.application.repository.MemberDataAdaptor;
import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.application.repository.RoomMemberRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.Message;
import com.jungle.navigation.chat.persistence.entity.RoomMember;
import com.jungle.navigation.chat.persistence.entity.RoomType;
import com.jungle.navigation.chat.presentation.dto.response.CreateRoomResponse;
import com.jungle.navigation.chat.presentation.dto.response.EachMessage;
import com.jungle.navigation.chat.presentation.dto.response.MessagesResponse;
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
public class RoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final RoomMemberRepository roomMemberRepository;
	private final MessageRepository messageRepository;
	private final MemberDataAdaptor memberDataAdaptor;

	@Transactional
	public CreateRoomResponse createDirectRoom(Long senderId, Long oppositeId) {
		ChatRoom commonChatRoom =
				chatRoomRepository.findCommonChatRoom(senderId, oppositeId, RoomType.DIRECT);

		if (commonChatRoom == null) {
			Long chatRoomId = createNewChatRoom(senderId, oppositeId);
			return CreateRoomResponse.of(chatRoomId);
		}

		return CreateRoomResponse.of(commonChatRoom.getId());
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

	private Long createNewChatRoom(Long senderId, Long oppositeId) {
		ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.of(RoomType.DIRECT));
		roomMemberRepository.save(RoomMember.of(senderId, chatRoom.getId()));
		roomMemberRepository.save(RoomMember.of(oppositeId, chatRoom.getId()));

		return chatRoom.getId();
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
}
