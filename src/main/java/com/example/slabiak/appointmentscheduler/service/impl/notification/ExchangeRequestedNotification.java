package com.example.slabiak.appointmentscheduler.service.impl.notification;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

public class ExchangeRequestedNotification extends NotificationTemplate {
    private final Appointment oldAppointment;
    private final Appointment newAppointment;

    public ExchangeRequestedNotification(Appointment oldAppointment, Appointment newAppointment, NotificationService notificationService, EmailService emailService, boolean mailingEnabled) {
        super(notificationService, emailService, mailingEnabled);
        this.oldAppointment = oldAppointment;
        this.newAppointment = newAppointment;
    }

    @Override
    protected String getTitle() {
        return "Request for exchange";
    }

    @Override
    protected String getMessage() {
        return "One of the users sent you a request to exchange his appointment with your appointment";
    }

    @Override
    protected String getUrl() {
        return "/appointments/" + newAppointment.getId();
    }

    @Override
    protected User getUser() {
        return newAppointment.getCustomer();
    }

    @Override
    protected void sendEmailNotification() {
        emailService.sendNewExchangeRequestedNotification(oldAppointment, newAppointment);
    }
}
