package com.jungle.navigation.member;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member")
public class MemberEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private int memberId;

	private String username;
	private String email;

	@Column(name = "user_type")
	private char userType;

	private String id;
	private String pw;

	private float x;
	private float y;

	@Column(name = "last_login")
	private Timestamp lastLogin;

	@Column(name = "joined_at")
	private Timestamp joinedAt;

	private String direction;

	@Column(name = "google_identity")
	private String googleIdentity;

	@Column(name = "api_token")
	private String apiToken;
}
