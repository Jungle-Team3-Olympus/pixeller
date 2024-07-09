package com.jungle.navigation.chat.persistence.entity;

import com.jungle.navigation.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = Message.PREFIX)
public class Message extends BaseEntity {
	public static final String PREFIX = "messages";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = Message.PREFIX + "_id")
	private Long id;

	@Column(name = "room_id")
	private Long roomId;

	@Column(name = "user_id")
	private Long senderId;

	@Column(name = "content")
	@Lob
	private String content;

	@Builder.Default
	@Column(name = "send_time")
	private LocalDateTime sendTime = LocalDateTime.now();
}
