package com.example.slabiak.appointmentscheduler.service.impl.notification;

import com.example.slabiak.appointmentscheduler.entity.ChatMessage;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

public class ChatMessageNotification extends NotificationTemplate {
    private final ChatMessage chatMessage;

    public ChatMessageNotification(ChatMessage chatMessage, NotificationService notificationService, EmailService emailService, boolean mailingEnabled) {
        super(notificationService, emailService, mailingEnabled);
        this.chatMessage = chatMessage;

    }

    @Override
    protected String getTitle() {
        return "New chat message";
    }

    @Override
    protected String getMessage() {
        return "You have new chat message from " + chatMessage.getAuthor().getFirstName() + " regarding appointment scheduled at " + chatMessage.getAppointment().getStart();
    }

    @Override
    protected String getUrl() {
        return "/appointments/" + chatMessage.getAppointment().getId();
    }

    @Override
    protected User getUser() {
        return chatMessage.getAuthor() == chatMessage.getAppointment().getProvider()
                ? chatMessage.getAppointment().getCustomer()
                : chatMessage.getAppointment().getProvider();
    }

    @Override
    protected void sendEmailNotification() {
        emailService.sendNewChatMessageNotification(chatMessage);
    }
}
