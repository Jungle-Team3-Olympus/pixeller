package com.jungle.navigation.chat.presentation;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.chat.application.RoomService;
import com.jungle.navigation.chat.presentation.dto.request.CreateDirectRoomRequest;
import com.jungle.navigation.chat.presentation.dto.response.CreateRoomResponse;
import com.jungle.navigation.chat.presentation.support.RoomSuccessMessageCode;
import com.jungle.navigation.common.presentation.respnose.ApiResponse;
import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.jungle.navigation.common.presentation.respnose.ApiResponseGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

	private final RoomService roomService;

	/**
	 * direct chat을 생성
	 *
	 * @param memberInfo
	 * @param request 상대방의 member 정보를 전달
	 */
	@PostMapping("/api/chat/direct")
	public ApiResponse<SuccessBody<CreateRoomResponse>> joinDirectRoom(
			@Member MemberInfo memberInfo, @RequestBody CreateDirectRoomRequest request) {
		CreateRoomResponse response = roomService.createDirectRoom(memberInfo.getMemberId(), request);
		return ApiResponseGenerator.success(
				response, HttpStatus.OK, RoomSuccessMessageCode.CREATE_ROOM);
	}
}
