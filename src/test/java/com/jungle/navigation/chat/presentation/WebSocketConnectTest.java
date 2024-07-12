package com.jungle.navigation.chat.presentation;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WebSocketConnectTest {

	@LocalServerPort private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

	@DisplayName("웹 소켓 연결을 시도한다.")
	@Test
	void webSocketConnectTest() {
		ExtractableResponse<Response> response =
				given().log().all().when().get("/chat").then().log().all().extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}
}
