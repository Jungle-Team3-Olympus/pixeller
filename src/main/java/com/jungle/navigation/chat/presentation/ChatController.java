package com.jungle.navigation.chat.presentation;

import static com.jungle.navigation.chat.presentation.support.ChatConstants.MEMBER_ID;
import static com.jungle.navigation.chat.presentation.support.ChatConstants.MEMBER_NAME;
import static com.jungle.navigation.chat.support.WebSocketConstant.SUBSCRIBE;

import com.jungle.navigation.chat.application.ChatService;
import com.jungle.navigation.chat.presentation.dto.request.SendMessageRequest;
import com.jungle.navigation.chat.presentation.dto.response.MessageResponse;
import com.jungle.navigation.chat.presentation.dto.response.MessagesResponse;
import com.jungle.navigation.chat.presentation.dto.response.ReadMessageResponse;
import com.jungle.navigation.chat.presentation.support.RoomSuccessMessageCode;
import com.jungle.navigation.chat.presentation.support.SessionManager;
import com.jungle.navigation.common.presentation.respnose.ApiResponse;
import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.jungle.navigation.common.presentation.respnose.ApiResponseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
	private static final String PUBLIC_ROOM_UUID = "1";
	private static final String WELCOME_MESSAGE = "님이 입장했습니다.";
	private static final String SUB_PUBLIC_ROOM = SUBSCRIBE + "/message/" + PUBLIC_ROOM_UUID;

	private final ChatService chatService;
	private final SessionManager sessionManager;

	private final SimpMessageSendingOperations messagingTemplate;

	/**
	 * pub : pub/message/1/enter sub : sub/message/1
	 *
	 * <p>public 채팅방에 들어왔을 때 호출
	 */
	@MessageMapping("/message/" + PUBLIC_ROOM_UUID + "/enter")
	public void sendWelcomeMessage(SimpMessageHeaderAccessor headerAccessor) {
		String enterName = sessionManager.getValue(headerAccessor, MEMBER_NAME, String.class);
		MessageResponse response = MessageResponse.of(1L, enterName, enterName + WELCOME_MESSAGE);

		messagingTemplate.convertAndSend(SUB_PUBLIC_ROOM, response);
	}

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
		MessageResponse response = MessageResponse.of(1L, senderName, request.content());

		messagingTemplate.convertAndSend(SUB_PUBLIC_ROOM, response);
	}

	/**
	 * pub : pub/message/direct/{roomId} sub : sub/message/direct/{roomId}
	 *
	 * <p>특정 room에 direct message 전송
	 *
	 * @param headerAccessor
	 * @param request
	 */
	@MessageMapping("/message/direct/{roomId}")
	public void sendDirectMessageToRoom(
			@DestinationVariable("roomId") Long roomId,
			SimpMessageHeaderAccessor headerAccessor,
			@Payload SendMessageRequest request) {

		Long senderId = sessionManager.getValue(headerAccessor, MEMBER_ID, Long.class);
		String senderName = sessionManager.getValue(headerAccessor, MEMBER_NAME, String.class);

		MessageResponse response =
				chatService.createDirectMessage(senderId, senderName, roomId, request);
		messagingTemplate.convertAndSend(SUBSCRIBE + "/message/direct/" + roomId, response);
	}

	/** 메시지 읽음 표시 */
	@MessageMapping("/message/{messageId}/read")
	public void readMessage(
			@DestinationVariable("messageId") Long messageId, SimpMessageHeaderAccessor headerAccessor) {
		Long readerId = sessionManager.getValue(headerAccessor, MEMBER_ID, Long.class);

		ReadMessageResponse response = chatService.readMessage(readerId, messageId);
		messagingTemplate.convertAndSend(
				SUBSCRIBE + "/message/direct/" + response.chatRoomId(), response);
	}

	/**
	 * 해당 방의 메시지들을 가져온다
	 *
	 * @param roomId
	 * @return
	 */
	@GetMapping("/api/chat-room/{roomId}")
	public ApiResponse<SuccessBody<MessagesResponse>> getMessageByRoom(
			@PathVariable("roomId") Long roomId,
			@RequestParam(required = false, defaultValue = "0", value = "page") int pageNumber,
			@RequestParam(required = false, defaultValue = "10", value = "size") int pageSize) {
		MessagesResponse response = chatService.findByChatRoomId(roomId, pageNumber, pageSize);
		return ApiResponseGenerator.success(response, HttpStatus.OK, RoomSuccessMessageCode.GET_ROOM);
	}
}
