package com.example.slabiak.appointmentscheduler.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;

public interface AvailabilityService {
    List<TimePeroid> getAvailableHours(int providerId, int customerId, int workId, LocalDate date);
    List<TimePeroid> calculateAvailableHours(List<TimePeroid> availableTimePeroids, Work work, LocalDate date);
    List<TimePeroid> excludeAppointmentsFromTimePeroids(List<TimePeroid> periods, List<Appointment> appointments);
    boolean isAvailable(int workId, int providerId, int customerId, LocalDateTime start);
}
