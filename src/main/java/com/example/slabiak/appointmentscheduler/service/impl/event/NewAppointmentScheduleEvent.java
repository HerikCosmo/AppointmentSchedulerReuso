package com.example.slabiak.appointmentscheduler.service.impl.event;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

public class NewAppointmentScheduleEvent {
    private Appointment appointment;

    public NewAppointmentScheduleEvent(Appointment appointment) {
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }
}
