package com.jungle.navigation.chat.presentation.docs;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.chat.presentation.dto.response.RoomResponse;
import com.jungle.navigation.common.presentation.respnose.ApiResponse;
import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
}
