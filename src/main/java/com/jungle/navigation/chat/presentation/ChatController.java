package com.jungle.navigation.chat.presentation;

import static com.jungle.navigation.chat.presentation.support.ChatConstants.MEMBER_ID;
import static com.jungle.navigation.chat.presentation.support.ChatConstants.MEMBER_NAME;
import static com.jungle.navigation.chat.support.WebSocketEndpoints.PUBLIC_ROOM_UUID;

import com.jungle.navigation.chat.application.ChatService;
import com.jungle.navigation.chat.presentation.dto.request.GetPagesRequest;
import com.jungle.navigation.chat.presentation.dto.request.SendMessageRequest;
import com.jungle.navigation.chat.presentation.support.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;
	private final SessionManager sessionManager;

	/**
	 * pub : pub/message/1 sub : sub/message/1
	 *
	 * <p>public 채팅방에 메시지를 전송할 때 호출
	 *
	 * @param request 전송할 메시지 내용
	 */
	@MessageMapping("/message/" + PUBLIC_ROOM_UUID)
	public void sendPublicMessage(
			SimpMessageHeaderAccessor headerAccessor, @Payload SendMessageRequest request) {
		String senderName = sessionManager.getValue(headerAccessor, MEMBER_NAME, String.class);
		chatService.createPublicMessage(senderName, request);
	}

	/**
	 * pub : pub/message/direct/{roomId} sub : sub/message/direct/{roomId}
	 *
	 * <p>특정 room에 direct message 전송
	 *
	 * @param headerAccessor
	 * @param request
	 */
	@MessageMapping("/message/direct/{roomId}/send")
	public void sendDirectMessageToRoom(
			@DestinationVariable("roomId") Long roomId,
			SimpMessageHeaderAccessor headerAccessor,
			@Payload SendMessageRequest request) {

		Long senderId = sessionManager.getValue(headerAccessor, MEMBER_ID, Long.class);
		String senderName = sessionManager.getValue(headerAccessor, MEMBER_NAME, String.class);

		chatService.createDirectMessage(senderId, senderName, roomId, request);
	}

	/** 메시지 읽음 표시 */
	@MessageMapping("/message/{messageId}/read")
	public void readMessage(
			@DestinationVariable("messageId") Long messageId, SimpMessageHeaderAccessor headerAccessor) {
		Long readerId = sessionManager.getValue(headerAccessor, MEMBER_ID, Long.class);
		chatService.readMessage(readerId, messageId);
	}

	/** 해당 방의 메시지를 가져온다 */
	@MessageMapping("/message/direct/{roomId}")
	public void getMessageByRoom(@DestinationVariable Long roomId, @Payload GetPagesRequest request) {
		chatService.findByChatRoomId(roomId, request.page(), request.size());
	}
}
