package com.jungle.navigation.chat.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = ChatRoom.PREFIX)
public class ChatRoom {
	public static final String PREFIX = "chat_room";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ChatRoom.PREFIX + "_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "room_type", nullable = false)
	private RoomType roomType;

	@Column(unique = true, nullable = false)
	private String roomUUID;

	public static ChatRoom of(RoomType roomType, String roomUUID) {
		return ChatRoom.builder().roomType(roomType).roomUUID(roomUUID).build();
	}
}
