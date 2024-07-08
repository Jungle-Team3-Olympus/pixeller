package com.jungle.navigation.auth.persistence;

import com.jungle.navigation.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.sql.Timestamp;
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
public class Member extends BaseEntity {
	public static final String ENTITY_PREFIX = "member";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "id", nullable = false, length = 50)
	private String nickname;

	@Column(name = "user_type", nullable = false)
	@Builder.Default
	private char userType = 'U';

	@Column(name = "x")
	private Float x;

	@Column(name = "y")
	private Float y;

	@Column(name = "joined_at")
	private Timestamp joinedAt;

	@Column(name = "pw", length = 200)
	private String password;
}
