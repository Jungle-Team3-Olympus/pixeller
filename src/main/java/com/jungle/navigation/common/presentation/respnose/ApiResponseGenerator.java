package com.jungle.navigation.common.presentation.respnose;

import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.FailureBody;
import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.SuccessBody;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@UtilityClass
public class ApiResponseGenerator {

	public static ApiResponse<SuccessBody<Void>> success(final HttpStatus status, MessageCode code) {
		return new ApiResponse<>(
				new SuccessBody<>(true, code.getCode(), null, code.getMessage()), status);
	}

	public static <D> ApiResponse<SuccessBody<D>> success(
			final D data, final HttpStatus status, MessageCode code) {
		return new ApiResponse<>(
				new SuccessBody<>(true, code.getCode(), data, code.getMessage()), status);
	}

	public static ApiResponse<FailureBody> fail(final MessageCode code, final HttpStatus status) {
		return new ApiResponse<>(new FailureBody(false, code.getCode(), code.getMessage()), status);
	}

	public static ApiResponse<FailureBody> fail(
			BindingResult bindingResult, final String code, final HttpStatus status) {
		return new ApiResponse<>(
				new FailureBody(false, code, createErrorMessage(bindingResult)), status);
	}

	private static String createErrorMessage(BindingResult bindingResult) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;

		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			if (!isFirst) {
				sb.append(", ");
			} else {
				isFirst = false;
			}
			sb.append("[");
			sb.append(fieldError.getField());
			sb.append("] ");
			sb.append(fieldError.getDefaultMessage());
		}

		return sb.toString();
	}
}
