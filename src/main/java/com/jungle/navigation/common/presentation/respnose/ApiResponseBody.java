package com.jungle.navigation.common.presentation.respnose;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ApiResponseBody {
	@Getter
	@AllArgsConstructor
	public static class FailureBody implements Serializable {

		private boolean isSuccess;
		private String code;
		private String message;
	}

	@Getter
	@AllArgsConstructor
	public static class SuccessBody<D> implements Serializable {
		private boolean isSuccess;
		private String code;
		private D data;
		private String message;
	}
}
