package com.jungle.navigation.chat.presentation;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.chat.application.RoomService;
import com.jungle.navigation.chat.presentation.dto.response.CreateRoomResponse;
import com.jungle.navigation.chat.presentation.support.RoomSuccessMessageCode;
import com.jungle.navigation.common.presentation.respnose.ApiResponse;
import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.jungle.navigation.common.presentation.respnose.ApiResponseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
