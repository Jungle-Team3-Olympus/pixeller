package com.jungle.navigation.chat.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.jungle.navigation.chat.persistence.entity.Message;
import com.jungle.navigation.chat.persistence.entity.RoomMember;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MessageJpaRepositoryTest {

	@Autowired private MessageJpaRepository messageRepository;

	@Autowired private RoomMemberJpaRepository roomMemberRepository;

	@Test
	@DisplayName("참여한 채팅방 목록을 메시지 시간의 역순으로 가져온다")
	void get_chat_rooms_by_send_time() {
		// given
		Long memberId1 = 1L;

		Long roomId1 = 1L;
		Long roomId2 = 2L;

		RoomMember room1Member = RoomMember.of(memberId1, roomId1);
		RoomMember room2Member = RoomMember.of(memberId1, roomId2);
		roomMemberRepository.saveAll(List.of(room1Member, room2Member));

		Message message1 =
				Message.builder()
						.roomId(roomId1)
						.senderId(memberId1)
						.content("First AbstractMessage")
						.sendTime(Timestamp.valueOf(LocalDateTime.now().minusHours(2)))
						.build();

		Message message2 =
				Message.builder()
						.roomId(roomId2)
						.senderId(memberId1)
						.content("Second AbstractMessage")
						.sendTime(Timestamp.valueOf(LocalDateTime.now()))
						.build();

		messageRepository.saveAll(List.of(message1, message2));

		// when
		Sort sort = Sort.by(Direction.ASC, "sendTime");
		Pageable pageable = PageRequest.of(0, 10, sort);
		Slice<Message> result = messageRepository.findRoomsByMemberId(memberId1, pageable);

		// Then

		List<Message> content = result.getContent();
		for (Message message : content) {
			System.out.println(message);
		}

		assertThat(result.getContent().get(0).getRoomId()).isEqualTo(roomId2);
		assertThat(result.getContent().get(1).getRoomId()).isEqualTo(roomId1);
	}

	@Test
	@DisplayName("내가 참여한 방의 채팅방 목록만 가져온다")
	void get_chat_rooms_filter_participate_member() {
		// given
		Long memberId1 = 1L;
		Long memberId2 = 2L;

		Long roomId1 = 1L;
		Long roomId2 = 2L;

		RoomMember room1Member1 = RoomMember.of(memberId1, roomId1);
		RoomMember room2Member2 = RoomMember.of(memberId2, roomId2);
		roomMemberRepository.saveAll(List.of(room1Member1, room2Member2));

		Message message1 =
				Message.builder()
						.roomId(roomId1)
						.senderId(memberId1)
						.content("First AbstractMessage")
						.sendTime(Timestamp.valueOf(LocalDateTime.now().minusHours(2)))
						.build();

		Message message2 =
				Message.builder()
						.roomId(roomId2)
						.senderId(memberId1)
						.content("Second AbstractMessage")
						.sendTime(Timestamp.valueOf(LocalDateTime.now()))
						.build();

		messageRepository.saveAll(List.of(message1, message2));

		// when
		Sort sort = Sort.by(Direction.ASC, "sendTime");
		Pageable pageable = PageRequest.of(0, 10, sort);
		Slice<Message> result = messageRepository.findRoomsByMemberId(memberId1, pageable);

		// Then
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getRoomId()).isEqualTo(roomId1);
	}

	void find_messages() {
		// given
		Long memberId1 = 1L;
		Long memberId2 = 2L;

		Long roomId = 1L;

		RoomMember roomMember1 = RoomMember.of(memberId1, roomId);
		RoomMember roomMember2 = RoomMember.of(memberId2, roomId);
		roomMemberRepository.saveAll(List.of(roomMember1, roomMember2));

		Message message1 =
				Message.builder()
						.roomId(roomId)
						.senderId(memberId1)
						.content("First AbstractMessage")
						.sendTime(Timestamp.valueOf(LocalDateTime.now().minusHours(2)))
						.build();

		Message message2 =
				Message.builder()
						.roomId(roomId)
						.senderId(memberId2)
						.content("Second AbstractMessage")
						.sendTime(Timestamp.valueOf(LocalDateTime.now()))
						.build();

		messageRepository.saveAll(List.of(message1, message2));

		// when
		Pageable pageable = PageRequest.of(0, 10, Sort.by("sendTime").descending());
		Slice<Message> result = messageRepository.findRoomsByMemberId(memberId1, pageable);

		// Then

		List<Message> content = result.getContent();
		for (Message message : content) {
			System.out.println(message);
		}

		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent().get(0).getContent()).isEqualTo("First AbstractMessage");
		assertThat(result.getContent().get(1).getContent()).isEqualTo("Second AbstractMessage");
	}
}
