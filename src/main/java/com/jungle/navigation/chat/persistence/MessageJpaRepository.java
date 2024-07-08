package com.jungle.navigation.chat.persistence;

import com.jungle.navigation.chat.application.repository.MessageRepository;
import com.jungle.navigation.chat.persistence.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageJpaRepository extends JpaRepository<Message, Long>, MessageRepository {}
