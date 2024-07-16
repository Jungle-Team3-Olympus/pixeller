package com.jungle.navigation.chat.presentation.docs;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.chat.presentation.dto.response.GetChatRoomsResponse;
import com.jungle.navigation.chat.presentation.dto.response.RoomResponse;
import com.jungle.navigation.common.presentation.respnose.ApiResponse;
import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.jungle.navigation.common.presentation.respnose.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅방 API", description = "채팅방 관련 API")
@RestController
@Validated
@RequestMapping("/api")
public interface RoomApi {
	@Operation(
			summary = "direct 채팅방 조회",
			description =
					"해당 상대방과 direct chatting room이 있을 경우 해당 채팅방 정보를 가져오며 direct chatting room이 없으면 새로운 채팅방을 생성한다.")
	@PostMapping("/chat-room/opposite/{oppositeId}")
	ApiResponse<SuccessBody<RoomResponse>> joinDirectRoom(
			@Member MemberInfo memberInfo,
			@PathVariable("oppositeId")
					@Parameter(name = "oppositeId", description = "채팅 상대방 Id", example = "1", required = true)
					Long oppositeId);

	@Operation(summary = "direct 채팅방 리스트 조회", description = "본인이 참여한 direct chatting room 리스트를 가져온다.")
	@SubscribeMapping("/chat-room/{memberId}?page={page}&size={size}")
	SliceResponse<GetChatRoomsResponse> getChatRooms(
			@Parameter(name = "memberId", description = "본인의 id", example = "1", required = true)
					@DestinationVariable
					Long memberId,
			@Parameter(
							name = "page",
							description = "몇 번째 페이지(0번째 페이지부터 시작)",
							example = "0",
							required = true)
					@DestinationVariable
					int page,
			@Parameter(name = "size", description = "한 페이지에 가져올 수", example = "10", required = true)
					@DestinationVariable
					int size);
}
