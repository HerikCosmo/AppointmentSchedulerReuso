package com.example.slabiak.appointmentscheduler.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import com.example.slabiak.appointmentscheduler.service.ScheduleService;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    
    @Override
    public List<TimePeroid> calculateAvailableHours(List<TimePeroid> availableTimePeroids, Work work) {
        ArrayList<TimePeroid> availableHours = new ArrayList<>();
        for (TimePeroid period : availableTimePeroids) {
            TimePeroid workPeriod = new TimePeroid(period.getStart(), period.getStart().plusMinutes(work.getDuration()));
            while (workPeriod.getEnd().isBefore(period.getEnd()) || workPeriod.getEnd().equals(period.getEnd())) {
                availableHours.add(new TimePeroid(workPeriod.getStart(), workPeriod.getStart().plusMinutes(work.getDuration())));
                workPeriod.setStart(workPeriod.getStart().plusMinutes(work.getDuration()));
                workPeriod.setEnd(workPeriod.getEnd().plusMinutes(work.getDuration()));
            }
        }
        return availableHours;
    }

    @Override
    public List<TimePeroid> excludeAppointmentsFromTimePeroids(List<TimePeroid> periods,
            List<Appointment> appointments) {
        List<TimePeroid> toAdd = new ArrayList<>();
        Collections.sort(appointments);
         for (Appointment appointment : appointments) {
            for (TimePeroid period : periods) {
                if ((appointment.getStart().toLocalTime().isBefore(period.getStart()) || appointment.getStart().toLocalTime().equals(period.getStart()))
                        && appointment.getEnd().toLocalTime().isAfter(period.getStart())
                        && appointment.getEnd().toLocalTime().isBefore(period.getEnd())) {
                    period.setStart(appointment.getEnd().toLocalTime());
                }
                if (appointment.getStart().toLocalTime().isAfter(period.getStart())
                        && appointment.getStart().toLocalTime().isBefore(period.getEnd())
                        && (appointment.getEnd().toLocalTime().isAfter(period.getEnd()) || appointment.getEnd().toLocalTime().equals(period.getEnd()))) {
                    period.setEnd(appointment.getStart().toLocalTime());
                }
                if (appointment.getStart().toLocalTime().isAfter(period.getStart())
                        && appointment.getEnd().toLocalTime().isBefore(period.getEnd())) {
                    toAdd.add(new TimePeroid(period.getStart(), appointment.getStart().toLocalTime()));
                    period.setStart(appointment.getEnd().toLocalTime());
                }
            }
        }
        periods.addAll(toAdd);
        Collections.sort(periods);
        return periods;
    }
}
