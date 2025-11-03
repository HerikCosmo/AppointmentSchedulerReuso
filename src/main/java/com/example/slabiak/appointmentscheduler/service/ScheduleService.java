package com.example.slabiak.appointmentscheduler.service;

import java.util.List;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;

public interface ScheduleService {
    List<TimePeroid> calculateAvailableHours(List<TimePeroid> availableTimePeroids, Work work);
    List<TimePeroid> excludeAppointmentsFromTimePeroids(List<TimePeroid> periods, List<Appointment> appointments);
}
