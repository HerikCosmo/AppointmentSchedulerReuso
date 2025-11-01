package com.example.slabiak.appointmentscheduler.service.impl.notification;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

public class AppointmentCanceledByCustomerNotification extends NotificationTemplate {
    private final Appointment appointment;

    public AppointmentCanceledByCustomerNotification(Appointment appointment, NotificationService notificationService, EmailService emailService, boolean mailingEnabled) {
        super(notificationService, emailService, mailingEnabled);
        this.appointment = appointment;
    }

    @Override
    protected String getTitle() {
        return "Appointment Canceled";
    }

    @Override
    protected String getMessage() {
        return appointment.getCustomer().getFirstName()
                + " "
                + appointment.getCustomer().getLastName()
                + " cancelled appointment scheduled at "
                + appointment.getStart().toString();
    }

    @Override
    protected String getUrl() {
        return "/appointments/" + appointment.getId();
    }

    @Override
    protected User getUser() {
        return appointment.getProvider();
    }

    @Override
    protected void sendEmailNotification() {
        this.emailService.sendAppointmentCanceledByCustomerNotification(appointment);
    }
}
