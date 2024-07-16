package com.jungle.navigation.chat.presentation;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.chat.application.RoomService;
import com.jungle.navigation.chat.presentation.docs.RoomApi;
import com.jungle.navigation.chat.presentation.dto.response.GetChatRoomsResponse;
import com.jungle.navigation.chat.presentation.dto.response.RoomResponse;
import com.jungle.navigation.chat.presentation.support.RoomSuccessMessageCode;
import com.jungle.navigation.common.presentation.respnose.ApiResponse;
import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.jungle.navigation.common.presentation.respnose.ApiResponseGenerator;
import com.jungle.navigation.common.presentation.respnose.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoomController implements RoomApi {

	private final RoomService roomService;

	@Override
	@PostMapping("/chat-room/opposite/{oppositeId}")
	public ApiResponse<SuccessBody<RoomResponse>> joinDirectRoom(
			@Member MemberInfo memberInfo, @PathVariable("oppositeId") Long oppositeId) {
		RoomResponse response = roomService.createDirectRoom(memberInfo.getMemberId(), oppositeId);

		return ApiResponseGenerator.success(
				response, HttpStatus.OK, RoomSuccessMessageCode.CREATE_ROOM);
	}

	@Override
	@SubscribeMapping("/chat-room/{memberId}?page={page}&size={size}")
	public SliceResponse<GetChatRoomsResponse> getChatRooms(
			@DestinationVariable Long memberId,
			@DestinationVariable int page,
			@DestinationVariable int size) {
		return roomService.getChatRooms(memberId, page, size);
	}
}
