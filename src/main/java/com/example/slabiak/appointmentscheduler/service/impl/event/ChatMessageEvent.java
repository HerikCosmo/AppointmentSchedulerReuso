package com.example.slabiak.appointmentscheduler.service.impl.event;

import com.example.slabiak.appointmentscheduler.entity.ChatMessage;

public class ChatMessageEvent {
    private ChatMessage chatMessage;

    public ChatMessageEvent(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
