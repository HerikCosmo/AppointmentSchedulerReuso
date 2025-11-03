package com.example.slabiak.appointmentscheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slabiak.appointmentscheduler.dao.AppointmentRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.AppointmentStatus;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.AppointmentRejectionService;
import com.example.slabiak.appointmentscheduler.service.UserService;

@Service
public class AppointmentRejectionServiceImpl implements AppointmentRejectionService {
    private final AppointmentRepository appointmentRepository;
    private final UserService userService;

    @Autowired
    public AppointmentRejectionServiceImpl(AppointmentRepository appointmentRepository, UserService userService){
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
    }

    @Override
    public String getCancelNotAllowedReason(int userId, int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if(appointment.isAlreadyCanceled()){
            return "O agendamento já foi cancelado.";
        }

        if(!appointment.isOwnedByCustomer(userId) && !appointment.isOwnedByProvider(userId)){
            return "Você não tem permissão para cancelar este agendamento.";
        }
        return null;
    }

    @Override
    public void cancelUserAppointmentById(int appointmentId, int userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));

        User user = userService.getUserById(userId);

        String reason = getCancelNotAllowedReason(userId, appointmentId);

        if(reason != null){
            throw new IllegalStateException(reason);
        }
        appointment.cancel(user);
        appointmentRepository.save(appointment);
    }

    @Override
    public boolean isCustomerAllowedToRejectAppointment(int customerId, int appointmentId) {
       Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));

        return appointment.isOwnedByCustomer(customerId) && appointment.isPendingRejection();
    }

    @Override
    public boolean requestAppointmentRejection(int appointmentId, int customerId) {
        if(!isCustomerAllowedToRejectAppointment(customerId, appointmentId)) return false;

        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));
        
        appointment.setRejectionRequested(true);
        appointmentRepository.save(appointment);
        return true;
    }

    @Override
    public boolean isProviderAllowedToAcceptRejection(int providerId, int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));

        return appointment.isOwnedByProvider(providerId) && appointment.isRejectionRequested();
    }

    @Override
    public boolean acceptRejection(int appointmentId, int providerId) {
        if(!isProviderAllowedToAcceptRejection(providerId, appointmentId)) return false;

        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));
            
        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(appointment);
        return true;

    }
}
