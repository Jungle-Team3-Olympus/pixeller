package com.jungle.navigation.chat.application;

import static com.jungle.navigation.chat.support.WebSocketEndpoints.getChatRoomsDestination;

import com.jungle.navigation.chat.application.publisher.MessagePublisher;
import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.application.repository.MemberDataAdaptor;
import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.application.repository.RoomMemberRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.Message;
import com.jungle.navigation.chat.persistence.entity.RoomMember;
import com.jungle.navigation.chat.persistence.entity.RoomType;
import com.jungle.navigation.chat.presentation.dto.response.GetChatRoomsResponse;
import com.jungle.navigation.chat.presentation.dto.response.RoomResponse;
import com.jungle.navigation.common.presentation.respnose.SliceResponse;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	private final MessagePublisher messagePublisher;

	@Transactional
	public RoomResponse createDirectRoom(Long senderId, Long oppositeId) {
		ChatRoom commonChatRoom =
				chatRoomRepository.findCommonChatRoom(senderId, oppositeId, RoomType.DIRECT);

		if (commonChatRoom == null) {
			Long chatRoomId = createNewChatRoom(senderId, oppositeId);
			return RoomResponse.of(chatRoomId);
		}

		return RoomResponse.of(commonChatRoom.getId());
	}

	public void getChatRooms(Long memberId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Slice<Message> messages = messageRepository.findRoomsByMemberId(memberId, pageable);
		Map<Long, String> oppositesName = getOppositeName(messages.getContent(), memberId);

		List<GetChatRoomsResponse> response =
				messages.stream()
						.map(
								message ->
										new GetChatRoomsResponse(
												message.getRoomId(),
												oppositesName.get(message.getRoomId()),
												message.getContent(),
												message.getReadCount()))
						.toList();

		SliceResponse<GetChatRoomsResponse> sliceResponse =
				SliceResponse.of(
						response,
						messages.getNumber(),
						messages.getSize(),
						messages.isFirst(),
						messages.isLast());

		messagePublisher.send(getChatRoomsDestination(memberId), sliceResponse);
	}

	private Long createNewChatRoom(Long senderId, Long oppositeId) {
		ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.of(RoomType.DIRECT));
		roomMemberRepository.save(RoomMember.of(senderId, chatRoom.getId()));
		roomMemberRepository.save(RoomMember.of(oppositeId, chatRoom.getId()));

		return chatRoom.getId();
	}

	private Map<Long, String> getOppositeName(List<Message> messages, Long memberId) {
		List<Long> roomIds = getRoomIds(messages);
		List<RoomMember> roomMembers = roomMemberRepository.findByChatRoomIdIn(roomIds);
		Map<Long, Long> oppositeMemberIds = getOppositeMemberIds(roomMembers, memberId);

		return getOppositeName(oppositeMemberIds);
	}

	private List<Long> getRoomIds(List<Message> messages) {
		return messages.stream().map(Message::getRoomId).toList();
	}

	private Map<Long, Long> getOppositeMemberIds(List<RoomMember> roomMembers, Long memberId) {
		return roomMembers.stream()
				.filter(member -> member.isOtherMember(memberId))
				.collect(Collectors.toMap(RoomMember::getChatRoomId, RoomMember::getMemberId));
	}

	private Map<Long, String> getOppositeName(Map<Long, Long> oppositeMemberIds) {
		return oppositeMemberIds.entrySet().stream()
				.collect(
						Collectors.toMap(
								Entry::getKey, entry -> memberDataAdaptor.getMember(entry.getValue())));
	}
}
