package com.example.slabiak.appointmentscheduler.service;

public interface AppointmentStatusService {
    void updateUserAppointmentsStatuses(int userId);
    void updateAllAppointmentsStatuses();
    void updateAppointmentsStatusesWithExpiredExchangeRequest();
}
