package com.example.slabiak.appointmentscheduler.service.impl.notification;

import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

public abstract class NotificationTemplate {
    private final NotificationService notificationService;
    protected final EmailService emailService;
    private final boolean mailingEnabled;

    public NotificationTemplate(NotificationService notificationService, EmailService emailService, boolean mailingEnabled) {
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.mailingEnabled = mailingEnabled;
    }

    public final void sendNotification(boolean sendEmail) {
        String title = getTitle();
        String message = getMessage();
        String url = getUrl();
        User user = getUser();

        this.notificationService.newNotification(title, message, url, user);

        if(sendEmail && mailingEnabled) {
            sendEmailNotification();
        }
    }

    protected abstract String getTitle();
    protected abstract String getMessage();
    protected abstract String getUrl();
    protected abstract User getUser();
    protected abstract void sendEmailNotification();
}
