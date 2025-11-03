package com.example.slabiak.appointmentscheduler.service.facade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.ChatMessage;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import com.example.slabiak.appointmentscheduler.service.AppointmentChatService;
import com.example.slabiak.appointmentscheduler.service.AppointmentManagementService;
import com.example.slabiak.appointmentscheduler.service.AppointmentRejectionService;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.AppointmentStatusService;
import com.example.slabiak.appointmentscheduler.service.AvailabilityService;
import com.example.slabiak.appointmentscheduler.service.JwtTokenService;
import com.example.slabiak.appointmentscheduler.service.impl.JwtTokenServiceImpl;

@Service
public class AppointmentFacade implements AppointmentService {
    private final AppointmentManagementService managementService;            
    private final AvailabilityService availabilityService;
    private final AppointmentRejectionService rejectionService;
    private final AppointmentChatService chatService;
    private final AppointmentStatusService statusService;
    private final JwtTokenService jwtTokenService;

    public AppointmentFacade(AppointmentManagementService managementService,
                             AvailabilityService availabilityService,
                             AppointmentRejectionService rejectionService,
                             AppointmentChatService chatService,
                             AppointmentStatusService statusService,
                             JwtTokenServiceImpl jwtTokenService) {
        this.managementService = managementService;
        this.availabilityService = availabilityService;
        this.rejectionService = rejectionService;
        this.chatService = chatService;
        this.statusService = statusService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void createNewAppointment(int workId, int providerId, int customerId, LocalDateTime start) {
        managementService.createNewAppointment(workId, providerId, customerId, start);
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        managementService.updateAppointment(appointment);
    }

    @Override
    public void updateUserAppointmentsStatuses(int userId) {
        statusService.updateUserAppointmentsStatuses(userId);
    }

    @Override
    public void updateAllAppointmentsStatuses() {
        statusService.updateAllAppointmentsStatuses();
    }

    @Override
    public void updateAppointmentsStatusesWithExpiredExchangeRequest() {
        statusService.updateAppointmentsStatusesWithExpiredExchangeRequest();
    }

    @Override
    public void deleteAppointmentById(int appointmentId) {
        managementService.deleteAppointmentById(appointmentId);
    }

    @Override
    @PostAuthorize("returnObject.provider.id == principal.id or returnObject.customer.id == principal.id or hasRole('ADMIN')")
    public Appointment getAppointmentByIdWithAuthorization(int id) {
        return managementService.getAppointmentById(id);
    }

    @Override
    public Appointment getAppointmentById(int id) {
        return managementService.getAppointmentById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Appointment> getAllAppointments() {
        return managementService.getAllAppointments();
    }

    @Override
    @PreAuthorize("#customerId == principal.id")
    public List<Appointment> getAppointmentByCustomerId(int customerId) {
        return managementService.getAppointmentByCustomerId(customerId);
    }

    @Override
    @PreAuthorize("#providerId == principal.id")
    public List<Appointment> getAppointmentByProviderId(int providerId) {
        return managementService.getAppointmentByProviderId(providerId);
    }

    @Override
    public List<Appointment> getAppointmentsByProviderAtDay(int providerId, LocalDate day) {
        return managementService.getAppointmentsByProviderAtDay(providerId, day);
    }

    @Override
    public List<Appointment> getAppointmentsByCustomerAtDay(int providerId, LocalDate day) {
        return managementService.getAppointmentsByProviderAtDay(providerId, day);
    }

    @Override
    public List<TimePeroid> getAvailableHours(int providerId, int customerId, int workId, LocalDate date) {
        return availabilityService.getAvailableHours(providerId, customerId, workId, date);
    }

    @Override
    public void addMessageToAppointmentChat(int appointmentId, int authorId, ChatMessage chatMessage) {
        chatService.addMessageToAppointmentChat(appointmentId, authorId, chatMessage);
    }

    @Override
    public List<TimePeroid> calculateAvailableHours(List<TimePeroid> availableTimePeroids, Work work) {
        return availabilityService.calculateAvailableHours(availableTimePeroids, work, LocalDate.now());
    }

    @Override
    public List<TimePeroid> excludeAppointmentsFromTimePeroids(List<TimePeroid> peroids,
            List<Appointment> appointments) {
        return availabilityService.excludeAppointmentsFromTimePeroids(peroids, appointments);
    }

    @Override
    public List<Appointment> getCanceledAppointmentsByCustomerIdForCurrentMonth(int userId) {
        return managementService.getCanceledAppointmentsByCustomerIdForCurrentMonth(userId);
    }

    @Override
    public void cancelUserAppointmentById(int appointmentId, int userId) {
        rejectionService.cancelUserAppointmentById(appointmentId, userId);
    }

    @Override
    public boolean isCustomerAllowedToRejectAppointment(int customerId, int appointmentId) {
        return rejectionService.isCustomerAllowedToRejectAppointment(customerId, appointmentId);
    }

    @Override
    public boolean requestAppointmentRejection(int appointmentId, int customerId) {
        return rejectionService.requestAppointmentRejection(appointmentId, customerId);
    }

    @Override
    public boolean requestAppointmentRejection(String token) {
        if(jwtTokenService.validateToken(token)) {
            int appointmentId = jwtTokenService.getAppointmentIdFromToken(token);
            int customerId = jwtTokenService.getCustomerIdFromToken(token);
            return requestAppointmentRejection(appointmentId, customerId);
        }
        return false;
    }

    @Override
    public boolean isProviderAllowedToAcceptRejection(int providerId, int appointmentId) {
        return rejectionService.isProviderAllowedToAcceptRejection(providerId, appointmentId);
    }

    @Override
    public boolean acceptRejection(int appointmentId, int providerId) {
        return rejectionService.acceptRejection(appointmentId, providerId);
    }

    @Override
    public boolean acceptRejection(String token) {
        if(jwtTokenService.validateToken(token)){
            int appointmentId = jwtTokenService.getAppointmentIdFromToken(token);
            int providerId = jwtTokenService.getProviderIdFromToken(token);
            return acceptRejection(appointmentId, providerId);
        }
        return false;
    }

    @Override
    public String getCancelNotAllowedReason(int userId, int appointmentId) {
        return rejectionService.getCancelNotAllowedReason(userId, appointmentId);
    }

    @Override
    public int getNumberOfCanceledAppointmentsForUser(int userId) {
        return managementService.getNumberOfCanceledAppointmentsForUser(userId);
    }

    @Override
    public int getNumberOfScheduledAppointmentsForUser(int userId) {
        return managementService.getNumberOfScheduledAppointmentsForUser(userId);
    }

    @Override
    public boolean isAvailable(int workId, int providerId, int customerId, LocalDateTime start) {
        return availabilityService.isAvailable(workId, providerId, customerId, start);
    }

    @Override
    public List<Appointment> getConfirmedAppointmentsByCustomerId(int customerId) {
        return managementService.getConfirmedAppointmentsByCustomerId(customerId);
    }
}
