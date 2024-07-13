package com.jungle.navigation.chat.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = RoomMember.PREFIX)
public class RoomMember {
	public static final String PREFIX = "room_member";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = RoomMember.PREFIX + "_id")
	private Long id;

	@Column(name = "user_id")
	private Long memberId;

	@Column(name = "room_id")
	private Long chatRoomId;

	public static RoomMember of(Long memberId, Long chatRoomId) {
		return RoomMember.builder().memberId(memberId).chatRoomId(chatRoomId).build();
	}
}
