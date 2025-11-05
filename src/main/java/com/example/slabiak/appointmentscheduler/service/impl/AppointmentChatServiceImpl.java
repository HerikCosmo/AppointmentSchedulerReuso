package com.example.slabiak.appointmentscheduler.service.impl;

import java.time.LocalDateTime;

import com.example.slabiak.appointmentscheduler.service.impl.event.ChatMessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.slabiak.appointmentscheduler.dao.ChatMessageRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.ChatMessage;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.AppointmentManagementService;
import com.example.slabiak.appointmentscheduler.service.AppointmentChatService;
import com.example.slabiak.appointmentscheduler.service.UserService;

@Service
@Transactional
public class AppointmentChatServiceImpl implements AppointmentChatService{
    private final AppointmentManagementService managementService;
    private final UserService userService;
    private final ChatMessageRepository chatMessageRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AppointmentChatServiceImpl(AppointmentManagementService managementService,
                                      UserService userService,
                                      ChatMessageRepository chatMessageRepository,
                                      ApplicationEventPublisher eventPublisher) {
        this.managementService = managementService;
        this.userService = userService;
        this.chatMessageRepository = chatMessageRepository;
        this.eventPublisher = eventPublisher;
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
            eventPublisher.publishEvent(new ChatMessageEvent(chatMessage));
        } else {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }  
    }
}
