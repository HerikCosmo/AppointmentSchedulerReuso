package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.ChatMessage;

public interface AppointmentChatService {
    void addMessageToAppointmentChat(int appointmentId, int authorId, ChatMessage chatMessage);
}
