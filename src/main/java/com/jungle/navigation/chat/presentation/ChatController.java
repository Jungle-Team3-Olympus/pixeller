package com.jungle.navigation.chat.presentation;

import static com.jungle.navigation.chat.presentation.support.ChatConstants.MEMBER_ID;
import static com.jungle.navigation.chat.presentation.support.ChatConstants.MEMBER_NAME;
import static com.jungle.navigation.chat.support.WebSocketConstant.SUBSCRIBE;

import com.jungle.navigation.chat.application.ChatService;
import com.jungle.navigation.chat.presentation.dto.request.SendMessageRequest;
import com.jungle.navigation.chat.presentation.dto.response.MessageResponse;
import com.jungle.navigation.chat.presentation.support.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
	private static final String PUBLIC_ROOM_UUID = "1";

	private final ChatService chatService;
	private final SessionManager sessionManager;

	private final SimpMessageSendingOperations messagingTemplate;

	@MessageMapping("/message/" + PUBLIC_ROOM_UUID)
	public void sendPublicMessage(
			SimpMessageHeaderAccessor headerAccessor, @Payload SendMessageRequest request) {

		Long senderId = sessionManager.getValue(headerAccessor, MEMBER_ID, Long.class);
		String senderName = sessionManager.getValue(headerAccessor, MEMBER_NAME, String.class);

		MessageResponse response =
				chatService.createPublicMessage(
						senderId, senderName, Long.valueOf(PUBLIC_ROOM_UUID), request);

		messagingTemplate.convertAndSend(SUBSCRIBE + "/message/" + PUBLIC_ROOM_UUID, response);
	}

	@MessageMapping("/message/direct")
	public void sendDirectMessage(
			SimpMessageHeaderAccessor headerAccessor, @Payload SendMessageRequest request) {

		Long senderId = sessionManager.getValue(headerAccessor, MEMBER_ID, Long.class);
		String senderName = sessionManager.getValue(headerAccessor, MEMBER_NAME, String.class);
		Long receiverId = request.receiverId();

		MessageResponse response =
				chatService.createDirectMessage(senderId, senderName, receiverId, request);
		messagingTemplate.convertAndSend(SUBSCRIBE + "/message/member" + receiverId, response);
	}
}
