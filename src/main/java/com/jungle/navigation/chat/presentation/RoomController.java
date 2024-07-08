package com.jungle.navigation.chat.presentation;

import static com.jungle.navigation.chat.presentation.support.ChatConstants.MEMBER_NAME;
import static com.jungle.navigation.chat.support.WebSocketConstant.SUBSCRIBE;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.chat.application.RoomService;
import com.jungle.navigation.chat.presentation.dto.request.CreateDirectRoomRequest;
import com.jungle.navigation.chat.presentation.dto.response.CreateRoomResponse;
import com.jungle.navigation.chat.presentation.dto.response.MessageResponse;
import com.jungle.navigation.chat.presentation.support.RoomSuccessMessageCode;
import com.jungle.navigation.chat.presentation.support.SessionManager;
import com.jungle.navigation.common.presentation.respnose.ApiResponse;
import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.jungle.navigation.common.presentation.respnose.ApiResponseGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {
	private static final String PUBLIC_ROOM_UUID = "1L";

	private final RoomService roomService;
	private final SessionManager sessionManager;

	/** public chatting을 subscribe 했을 때 welcome 메시지를 전달 */
	@SubscribeMapping("/message/" + PUBLIC_ROOM_UUID)
	@SendTo(SUBSCRIBE + "/message/" + PUBLIC_ROOM_UUID)
	public MessageResponse joinPublicRoom(SimpMessageHeaderAccessor headerAccessor) {
		String memberName = sessionManager.getValue(headerAccessor, MEMBER_NAME, String.class);
		return roomService.joinRoom(memberName);
	}

	/**
	 * direct chat을 생성
	 *
	 * @param memberId
	 * @param request 상대방의 member 정보를 전달
	 */
	@PostMapping("/api/chat/direct")
	public ApiResponse<SuccessBody<CreateRoomResponse>> joinDirectRoom(
			@Member Long memberId, @RequestBody CreateDirectRoomRequest request) {
		CreateRoomResponse response = roomService.createDirectRoom(memberId, request);
		return ApiResponseGenerator.success(
				response, HttpStatus.OK, RoomSuccessMessageCode.CREATE_ROOM);
	}
}
