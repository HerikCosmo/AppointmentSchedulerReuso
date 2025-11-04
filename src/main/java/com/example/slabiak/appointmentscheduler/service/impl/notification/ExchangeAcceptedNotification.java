package com.example.slabiak.appointmentscheduler.service.impl.notification;

import com.example.slabiak.appointmentscheduler.entity.ExchangeRequest;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

public class ExchangeAcceptedNotification extends NotificationTemplate {
    private final ExchangeRequest exchangeRequest;

    public ExchangeAcceptedNotification(ExchangeRequest exchangeRequest, NotificationService notificationService, EmailService emailService, boolean mailingEnabled) {
        super(notificationService, emailService, mailingEnabled);
        this.exchangeRequest = exchangeRequest;
    }

    @Override
    protected String getTitle() {
        return "Exchange request accepted";
    }

    @Override
    protected String getMessage() {
        return "Someone accepted your appointment exchange request from " + exchangeRequest.getRequested().getStart() + " to " + exchangeRequest.getRequestor().getStart();
    }

    @Override
    protected String getUrl() {
        return "/appointments/" + exchangeRequest.getRequested();
    }

    @Override
    protected User getUser() {
        return exchangeRequest.getRequested().getCustomer();
    }

    @Override
    protected void sendEmailNotification() {
        emailService.sendExchangeRequestAcceptedNotification(exchangeRequest);
    }
}
