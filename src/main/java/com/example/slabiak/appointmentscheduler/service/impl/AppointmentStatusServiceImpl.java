package com.example.slabiak.appointmentscheduler.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.slabiak.appointmentscheduler.dao.AppointmentRepository;
import com.example.slabiak.appointmentscheduler.entity.AppointmentStatus;
import com.example.slabiak.appointmentscheduler.service.AppointmentStatusService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

@Service
@Transactional
public class AppointmentStatusServiceImpl implements AppointmentStatusService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentManagementServiceImpl appointmentManagementServiceImpl;
    private final NotificationService notificationService;


    public AppointmentStatusServiceImpl(AppointmentRepository appointmentRepository,
                                        AppointmentManagementServiceImpl appointmentManagementServiceImpl,
                                        NotificationService notificationService){

        this.appointmentRepository = appointmentRepository;
        this.appointmentManagementServiceImpl = appointmentManagementServiceImpl;
        this.notificationService = notificationService;
    }

    @Override
    public void updateUserAppointmentsStatuses(int userId) {
        appointmentRepository.findScheduledByUserIdWithEndBeforeDate(LocalDateTime.now(), userId)
            .forEach(appointment -> {
                appointment.setStatus(AppointmentStatus.FINISHED);
                appointmentRepository.save(appointment);
            });
        
        appointmentRepository.findFinishedByUserIdWithEndBeforeDate(LocalDateTime.now(), userId)
            .forEach(appointment -> {
                appointment.setStatus(AppointmentStatus.INVOICED);
                appointmentRepository.save(appointment);
            });
    }

    @Override
    public void updateAllAppointmentsStatuses() {
        appointmentRepository.findScheduledWithEndBeforeDate(LocalDateTime.now())
            .forEach(appointment -> {
                appointment.setStatus(AppointmentStatus.FINISHED);
                appointmentRepository.save(appointment);
                if(LocalDateTime.now().minusDays(1).isBefore(appointment.getEnd())){
                    notificationService.newAppointmentFinishedNotification(appointment, true);
                }
            });

        appointmentRepository.findFinishedWithEndBeforeDate(LocalDateTime.now().minusDays(1))
            .forEach(appointment ->{
                appointment.setStatus(AppointmentStatus.CONFIRMED);
                appointmentRepository.save(appointment);
            });
    }

    @Override
    public void updateAppointmentsStatusesWithExpiredExchangeRequest() {
        appointmentRepository.findExchangeRequestedWithStartBefore(LocalDateTime.now().plusDays(1))
            .forEach(appointment -> {
                appointment.setStatus(AppointmentStatus.SCHEDULED);
                appointmentRepository.save(appointment);
            });
    }
}
