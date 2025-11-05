package com.example.slabiak.appointmentscheduler.service.impl.event;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

public class ExchangeRequestedEvent {
    private Appointment oldAppointment;
    private Appointment newAppointment;

    public ExchangeRequestedEvent(Appointment oldAppointment, Appointment newAppointment) {
        this.oldAppointment = oldAppointment;
        this.newAppointment = newAppointment;
    }

    public Appointment getOldAppointment() {
        return oldAppointment;
    }

    public Appointment getNewAppointment() {
        return newAppointment;
    }
}
