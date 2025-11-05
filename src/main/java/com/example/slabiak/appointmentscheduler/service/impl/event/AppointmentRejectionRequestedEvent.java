package com.example.slabiak.appointmentscheduler.service.impl.event;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

public class AppointmentRejectionRequestedEvent {
    private final Appointment appointment;

    public AppointmentRejectionRequestedEvent(Appointment appointment) {
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }
}
