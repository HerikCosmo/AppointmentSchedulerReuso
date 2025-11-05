package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.service.impl.event.AppointmentCanceledByCustomerEvent;
import com.example.slabiak.appointmentscheduler.service.impl.event.AppointmentCanceledByProviderEvent;
import com.example.slabiak.appointmentscheduler.service.impl.event.AppointmentRejectionAcceptedEvent;
import com.example.slabiak.appointmentscheduler.service.impl.event.AppointmentRejectionRequestedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AppointmentRejectionServiceImpl(AppointmentRepository appointmentRepository, UserService userService, ApplicationEventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
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

        if(user.equals(appointment.getCustomer())) {
            eventPublisher.publishEvent(new AppointmentCanceledByCustomerEvent(appointment));
        }

        if(user.equals(appointment.getProvider())) {
            eventPublisher.publishEvent(new AppointmentCanceledByProviderEvent(appointment));
        }

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
        eventPublisher.publishEvent(new AppointmentRejectionRequestedEvent(appointment));
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
        eventPublisher.publishEvent(new AppointmentRejectionAcceptedEvent(appointment));
        appointmentRepository.save(appointment);
        return true;

    }
}
