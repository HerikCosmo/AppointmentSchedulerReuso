package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.NotificationRepository;
import com.example.slabiak.appointmentscheduler.entity.*;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.impl.event.*;
import com.example.slabiak.appointmentscheduler.service.impl.notification.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final boolean mailingEnabled;

    public NotificationServiceImpl(@Value("${mailing.enabled}") boolean mailingEnabled, NotificationRepository notificationRepository, UserService userService, EmailService emailService) {
        this.mailingEnabled = mailingEnabled;
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public void newNotification(String title, String message, String url, User user) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setUrl(url);
        notification.setCreatedAt(new Date());
        notification.setMessage(message);
        notification.setUser(user);
        notificationRepository.save(notification);
    }


    @Override
    public void markAsRead(int notificationId, int userId) {
        Notification notification = notificationRepository.getOne(notificationId);
        if (notification.getUser().getId() == userId) {
            notification.setRead(true);
            notificationRepository.save(notification);
        } else {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
    }

    @Override
    public void markAllAsRead(int userId) {
        List<Notification> notifications = notificationRepository.getAllUnreadNotifications(userId);
        for (Notification notification : notifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public Notification getNotificationById(int notificationId) {
        return notificationRepository.getOne(notificationId);
    }

    @Override
    public List<Notification> getAll(int userId) {
        return userService.getUserById(userId).getNotifications();
    }

    @Override
    public List<Notification> getUnreadNotifications(int userId) {
        return notificationRepository.getAllUnreadNotifications(userId);
    }

    @EventListener
    public void handleAppointmentFinished(AppointmentFinishedEvent event) {
        new AppointmentFinishedNotification(event.getAppointment(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }

    @EventListener
    public void handleAppointmentRejectionRequested(AppointmentRejectionRequestedEvent event) {
        new AppointmentRejectionRequestedNotification(event.getAppointment(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }

    @EventListener
    public void handleNewAppointmentSchedule(NewAppointmentScheduleEvent event) {
        new AppointmentScheduledNotification(event.getAppointment(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }

    @EventListener
    public void handleAppointmentCanceledByCustomer(AppointmentCanceledByCustomerEvent event) {
        new AppointmentCanceledByCustomerNotification(event.getAppointment(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }

    @EventListener
    public void handleAppointmentCanceledByProvider(AppointmentCanceledByProviderEvent event) {
        new AppointmentCanceledByProviderNotification(event.getAppointment(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }

    @EventListener
    public void handleNewInvoice(InvoiceEvent event) {
        new InvoiceNotification(event.getInvoice(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }

    @EventListener
    public void handleExchangeRequest(ExchangeRequestedEvent event) {
        new ExchangeRequestedNotification(event.getOldAppointment(), event.getNewAppointment(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }

    @EventListener
    public void handleExchangeAccepted(ExchangeAcceptedEvent event) {
        new ExchangeAcceptedNotification(event.getExchangeRequest(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }

    @EventListener
    public void handleExchangeRejected(ExchangeRejectedEvent event) {
        new ExchangeRejectedNotification(event.getExchangeRequest(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }

    @EventListener
    public void handleAppointmentRejectAccepted(AppointmentRejectionAcceptedEvent event) {
        new AppointmentRejectionAcceptedNotification(event.getAppointment(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }

    @EventListener
    public void handleChatMessage(ChatMessageEvent event) {
        new ChatMessageNotification(event.getChatMessage(), this, emailService, mailingEnabled)
                .sendNotification(true);
    }
}
