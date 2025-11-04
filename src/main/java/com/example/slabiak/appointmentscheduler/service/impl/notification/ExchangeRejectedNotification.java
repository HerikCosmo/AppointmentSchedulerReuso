package com.example.slabiak.appointmentscheduler.service.impl.notification;

import com.example.slabiak.appointmentscheduler.entity.ExchangeRequest;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

public class ExchangeRejectedNotification extends NotificationTemplate {
    private final ExchangeRequest exchangeRequest;

    public ExchangeRejectedNotification(ExchangeRequest exchangeRequest, NotificationService notificationService, EmailService emailService, boolean mailingEnabled) {
        super(notificationService, emailService, mailingEnabled);
        this.exchangeRequest = exchangeRequest;

    }

    @Override
    protected String getTitle() {
        return "Exchange request rejected";
    }

    @Override
    protected String getMessage() {
        return "Someone rejected your appointment exchange request from " + exchangeRequest.getRequestor().getStart() + " to " + exchangeRequest.getRequested().getStart();
    }

    @Override
    protected String getUrl() {
        return  "/appointments/" + exchangeRequest.getRequestor();
    }

    @Override
    protected User getUser() {
        return exchangeRequest.getRequestor().getCustomer();
    }

    @Override
    protected void sendEmailNotification() {
        emailService.sendExchangeRequestRejectedNotification(exchangeRequest);
    }
}
