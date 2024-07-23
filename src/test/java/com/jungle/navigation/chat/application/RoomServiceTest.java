package com.jungle.navigation.chat.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jungle.navigation.chat.application.repository.ChatRoomRepository;
import com.jungle.navigation.chat.persistence.entity.RoomType;
import com.jungle.navigation.chat.presentation.dto.response.RoomResponse;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoomServiceTest {

	@Autowired private FacadeRoomService facadeRoomService;

	@Autowired private ChatRoomRepository chatRoomRepository;

	@Test
	void 두_유저가_동시에_서로의_채팅방_생성() throws InterruptedException {
		int numberOfThreads = 2;
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);

		Set<Long> createdRoomIds = Collections.synchronizedSet(new HashSet<>());
		AtomicInteger failCount = new AtomicInteger();

		Long senderId = 1L;
		Long oppositeId = 2L;

		for (int i = 0; i < numberOfThreads; i++) {
			executorService.submit(
					() -> {
						try {
							RoomResponse response = facadeRoomService.createDirectRoom(senderId, oppositeId);
							createdRoomIds.add(response.roomId());
						} catch (Exception e) {
							System.out.println(e.getMessage());
							failCount.incrementAndGet();
						} finally {
							latch.countDown();
						}
					});
		}

		latch.await();
		executorService.shutdown();

		assertEquals(1, createdRoomIds.size());
		assertDoesNotThrow(
				() -> chatRoomRepository.findCommonChatRoom(senderId, oppositeId, RoomType.DIRECT));
	}
}
