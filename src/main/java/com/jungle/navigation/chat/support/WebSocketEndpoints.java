package com.jungle.navigation.chat.support;

public class WebSocketEndpoints {
	public static final String SUBSCRIBE = "/sub";
	public static final String PUBLISH = "/pub";
	public static final String CONNECT = "/chat";

	public static String getDirectMessageDestination(Long roomId) {
		return SUBSCRIBE + "/message/direct/" + roomId;
	}

	public static String getChatRoomsDestination(Long memberId) {
		return SUBSCRIBE + "/chat-room/" + memberId;
	}
}
