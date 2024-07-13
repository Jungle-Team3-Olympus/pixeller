package com.jungle.navigation.chat.presentation;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.chat.application.RoomService;
import com.jungle.navigation.chat.presentation.dto.response.CreateRoomResponse;
import com.jungle.navigation.chat.presentation.dto.response.MessagesResponse;
import com.jungle.navigation.chat.presentation.support.RoomSuccessMessageCode;
import com.jungle.navigation.common.presentation.respnose.ApiResponse;
import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.jungle.navigation.common.presentation.respnose.ApiResponseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoomController {

	private final RoomService roomService;

	/**
	 * direct chat room을 가져온다
	 *
	 * @param memberInfo
	 * @param oppositeId
	 * @return
	 */
	@PostMapping("/chat-room/opposite/{oppositeId}")
	public ApiResponse<SuccessBody<CreateRoomResponse>> joinDirectRoom(
			@Member MemberInfo memberInfo, @PathVariable("oppositeId") Long oppositeId) {
		CreateRoomResponse response =
				roomService.createDirectRoom(memberInfo.getMemberId(), oppositeId);
		return ApiResponseGenerator.success(
				response, HttpStatus.OK, RoomSuccessMessageCode.CREATE_ROOM);
	}

	/**
	 * 해당 방의 메시지들을 가져온다
	 *
	 * @param roomId
	 * @return
	 */
	@GetMapping("/chatroom/{roomId}")
	public ApiResponse<SuccessBody<MessagesResponse>> getMessageByRoom(
			@PathVariable("roomId") Long roomId,
			@RequestParam(required = false, defaultValue = "0", value = "message") int messageId,
			@RequestParam(required = false, defaultValue = "10", value = "size") int pageSize) {
		MessagesResponse response = roomService.findByChatRoomId(roomId, messageId, pageSize);
		return ApiResponseGenerator.success(response, HttpStatus.OK, RoomSuccessMessageCode.GET_ROOM);
	}
}
