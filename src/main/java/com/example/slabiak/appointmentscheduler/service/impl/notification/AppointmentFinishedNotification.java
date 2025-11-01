package com.example.slabiak.appointmentscheduler.service.impl.notification;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

public class AppointmentFinishedNotification extends NotificationTemplate {
    private final Appointment appointment;

    public AppointmentFinishedNotification(Appointment appointment, NotificationService notificationService, EmailService emailService, boolean mailingEnabled) {
        super(notificationService, emailService, mailingEnabled);
        this.appointment = appointment;
    }

    @Override
    protected String getTitle() {
        return "Appointment Finished";
    }

    @Override
    protected String getMessage() {
        return "Appointment finished, you can reject that it took place until "
                + appointment.getEnd().plusHours(24);
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
        this.emailService.sendAppointmentFinishedNotification(appointment);
    }
}
