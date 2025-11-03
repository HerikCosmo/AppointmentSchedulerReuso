package com.example.slabiak.appointmentscheduler.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

public interface AppointmentManagementService {
    Appointment createNewAppointment(int workId, int providerId, int customerId, LocalDateTime start);
    void updateAppointment(Appointment appointment);
    void deleteAppointmentById(int id);
    Appointment getAppointmentById(int id);
    List<Appointment> getAllAppointments();
    List<Appointment> getAppointmentByCustomerId(int customerId);
    List<Appointment> getAppointmentByProviderId(int providerId);
    List<Appointment> getAppointmentsByProviderAtDay(int providerId, LocalDate day);
    List<Appointment> getAppointmentsByCustomerAtDay(int customerId, LocalDate day);
    List<Appointment> getConfirmedAppointmentsByCustomerId(int customerId);
    List<Appointment> getCanceledAppointmentsByCustomerIdForCurrentMonth(int customerId);
    int getNumberOfCanceledAppointmentsForUser(int userId);
    int getNumberOfScheduledAppointmentsForUser(int userId);
    
}
