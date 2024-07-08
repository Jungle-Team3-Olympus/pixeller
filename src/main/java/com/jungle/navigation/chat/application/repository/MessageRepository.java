package com.jungle.navigation.chat.application.repository;

import com.jungle.navigation.chat.persistence.entity.Message;

public interface MessageRepository {
	Message save(final Message message);
}
