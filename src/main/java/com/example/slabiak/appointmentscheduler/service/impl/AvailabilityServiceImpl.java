package com.example.slabiak.appointmentscheduler.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slabiak.appointmentscheduler.dao.AppointmentRepository;
import com.example.slabiak.appointmentscheduler.dao.WorkRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import com.example.slabiak.appointmentscheduler.service.AvailabilityService;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {
    private final AppointmentRepository appointmentRepository;
    private final WorkRepository workRepository;

    @Autowired
    public AvailabilityServiceImpl(AppointmentRepository appointmentRepository, WorkRepository workRepository){
        this.appointmentRepository = appointmentRepository;
        this.workRepository = workRepository;
    }

    @Override
    public List<TimePeroid> getAvailableHours(int providerId, int customerId, int workId, LocalDate date) {
        Work work = workRepository.findById(workId).orElseThrow(() -> new IllegalArgumentException("Work not found."));
        Provider provider = work.getProviders().get(workId);

        List<TimePeroid> providerTimeSlots = provider.getWorkingPlan().getAvailableTimePeroids(date);

        List<Appointment> appointments = appointmentRepository.findByProviderAndDay(providerId,date);

        List<TimePeroid> available = excludeAppointmentsFromTimePeroids(providerTimeSlots, appointments);

        return calculateAvailableHours(available, work, date);
    }

    @Override
    public List<TimePeroid> calculateAvailableHours(List<TimePeroid> availableTimePeroids, Work work, LocalDate date){
        List<TimePeroid> calculated = new ArrayList<>();
        int durationMinutes = work.getDuration();

        for(TimePeroid period: availableTimePeroids){
            LocalDateTime current = LocalDateTime.of(LocalDate.now(), period.getStart());
            LocalDateTime end = LocalDateTime.of(date, period.getEnd());
            while (current.plusMinutes(durationMinutes).isBefore(end) ||
                    current.plusMinutes(durationMinutes).equals(end)) {
                calculated.add(new TimePeroid(current.toLocalTime(), current.plusMinutes(durationMinutes).toLocalTime()));
                current = current.plusMinutes(durationMinutes);
            }
        }
        return calculated;
    }

    @Override
    public List<TimePeroid> excludeAppointmentsFromTimePeroids(List<TimePeroid> periods, List<Appointment> appointments) {
        List<TimePeroid> result = new ArrayList<>(periods);
        for (Appointment app: appointments){
            result.removeIf(period -> period.overLaps(app.getStart(), app.getEnd()));
        }
        return result;
    }

    @Override
    public boolean isAvailable(int workId, int providerId, int customerId, LocalDateTime start) {
        Work work = workRepository.findById(workId).orElseThrow(() -> new IllegalArgumentException("Work not found."));
        List<Appointment> appointments = appointmentRepository.findByProviderIdWithStartInPeroid(providerId, start, start);
        for (Appointment appointment : appointments) {
            if (start.isAfter(appointment.getStart()) && start.isBefore(appointment.getEnd())) {
                return false;
            }
        }
        return true;
    }
}
