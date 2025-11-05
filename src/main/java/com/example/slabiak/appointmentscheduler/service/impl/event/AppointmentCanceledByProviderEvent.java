package com.example.slabiak.appointmentscheduler.service.impl.event;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

public class AppointmentCanceledByProviderEvent {
    private Appointment appointment;

    public AppointmentCanceledByProviderEvent(Appointment appointment) {
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }
}
