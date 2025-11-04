package com.example.slabiak.appointmentscheduler.service.impl.notification;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

public class AppointmentRejectionAcceptedNotification extends NotificationTemplate {
    private final Appointment appointment;
    public AppointmentRejectionAcceptedNotification(Appointment appointment, NotificationService notificationService, EmailService emailService, boolean mailingEnabled) {
        super(notificationService, emailService, mailingEnabled);
        this.appointment = appointment;
    }

    @Override
    protected String getTitle() {
        return "Rejection accepted";
    }

    @Override
    protected String getMessage() {
        return "You provider accepted your rejection request";
    }

    @Override
    protected String getUrl() {
        return "/appointments/" + appointment.getId();
    }

    @Override
    protected User getUser() {
        return appointment.getCustomer();
    }

    @Override
    protected void sendEmailNotification() {
        emailService.sendAppointmentRejectionAcceptedNotification(appointment);
    }
}
