package com.jungle.navigation.chat.application;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.application.repository.MemberDataAdaptor;
import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.application.repository.RoomMemberRepository;
import com.jungle.navigation.chat.persistence.entity.ChatRoom;
import com.jungle.navigation.chat.persistence.entity.RoomMember;
import com.jungle.navigation.chat.persistence.entity.RoomType;
import com.jungle.navigation.chat.presentation.dto.response.CreateRoomResponse;
import lombok.RequiredArgsConstructor;
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

	private Long createNewChatRoom(Long senderId, Long oppositeId) {
		ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.of(RoomType.DIRECT));
		roomMemberRepository.save(RoomMember.of(senderId, chatRoom.getId()));
		roomMemberRepository.save(RoomMember.of(oppositeId, chatRoom.getId()));

		return chatRoom.getId();
	}
}
