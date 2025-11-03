package com.example.slabiak.appointmentscheduler.service;

public interface AppointmentRejectionService {
    String getCancelNotAllowedReason(int userId, int appointmentId);
    void cancelUserAppointmentById(int appointmentId, int userId);
    boolean isCustomerAllowedToRejectAppointment(int customerId, int appointmentId);
    boolean requestAppointmentRejection(int appointmentId, int customerId);
    boolean isProviderAllowedToAcceptRejection(int providerId, int appointmentId);
    boolean acceptRejection(int appointmentId, int providerId);
}
