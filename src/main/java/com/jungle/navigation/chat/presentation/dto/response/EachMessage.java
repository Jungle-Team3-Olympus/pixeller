package com.jungle.navigation.chat.presentation.dto.response;

public record EachMessage(Long messageId, String senderName, String message, int readCount) {}
