package com.example.slabiak.appointmentscheduler.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.slabiak.appointmentscheduler.dao.ChatMessageRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.ChatMessage;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.AppointmentManagementService;
import com.example.slabiak.appointmentscheduler.service.AppointmentChatService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;
import com.example.slabiak.appointmentscheduler.service.UserService;

@Service
@Transactional
public class AppointmentChatServiceImpl implements AppointmentChatService{
    private final AppointmentManagementService managementService;
    private final UserService userService;
    private final ChatMessageRepository chatMessageRepository;
    private final NotificationService notificationService;

    public AppointmentChatServiceImpl(AppointmentManagementService managementService,
                                      UserService userService,
                                      ChatMessageRepository chatMessageRepository,
                                      NotificationService notificationService) {
        this.managementService = managementService;
        this.userService = userService;
        this.chatMessageRepository = chatMessageRepository;
        this.notificationService = notificationService;
    }
    @Override
    public void addMessageToAppointmentChat(int appointmentId, int authorId, ChatMessage chatMessage) {
        Appointment appointment = managementService.getAppointmentById(appointmentId);

        if(appointment.getProvider().getId() == authorId || appointment.getCustomer().getId() == authorId){
            User author = userService.getUserById(authorId);
            chatMessage.setAuthor(author);
            chatMessage.setAppointment(appointment);
            chatMessage.setCreatedAt(LocalDateTime.now());
            chatMessageRepository.save(chatMessage);
            notificationService.newChatMessageNotification(chatMessage, true);
        } else {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }  
    }
}
