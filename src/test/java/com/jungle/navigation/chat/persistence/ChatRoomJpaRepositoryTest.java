package com.jungle.navigation.chat.persistence;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatRoomJpaRepositoryTest {
	@Autowired ChatRoomJpaRepository chatRoomJpaRepository;

	@Autowired RoomMemberJpaRepository roomMemberRepository;

	//	@Test
	//	void 두_유저간의_채팅방을_조회한다() {
	//		// given
	//		Long memberId1 = 1L;
	//		Long memberId2 = 2L;
	//
	//		Long roomId1 = 1L;
	//
	//		RoomMember room1Member = RoomMember.of(memberId1, roomId1);
	//		RoomMember room2Member = RoomMember.of(memberId2, roomId1);
	//		roomMemberRepository.saveAll(List.of(room1Member, room2Member));
	//
	//		ChatRoom chatRoom = ChatRoom.of(RoomType.DIRECT);
	//		ChatRoom expected = chatRoomJpaRepository.save(chatRoom);
	//
	//		// when
	//		ChatRoom commonChatRoom =
	//				chatRoomJpaRepository.findCommonChatRoom(memberId1, memberId2, RoomType.DIRECT);
	//
	//		// Then
	//		assertEquals(expected, commonChatRoom);
	//	}
}
