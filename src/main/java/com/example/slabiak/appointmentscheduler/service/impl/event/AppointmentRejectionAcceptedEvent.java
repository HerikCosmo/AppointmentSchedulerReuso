package com.example.slabiak.appointmentscheduler.service.impl.event;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

public class AppointmentRejectionAcceptedEvent {
    private final Appointment appointment;

    public AppointmentRejectionAcceptedEvent(Appointment appointment) {
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }
}
