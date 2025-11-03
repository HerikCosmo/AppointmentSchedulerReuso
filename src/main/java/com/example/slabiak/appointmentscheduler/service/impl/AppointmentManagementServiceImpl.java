package com.example.slabiak.appointmentscheduler.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.slabiak.appointmentscheduler.dao.AppointmentRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.AppointmentStatus;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.exception.AppointmentNotFoundException;
import com.example.slabiak.appointmentscheduler.service.AppointmentManagementService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkService;

@Service
@Transactional
public class AppointmentManagementServiceImpl implements AppointmentManagementService {
    private final AppointmentRepository appointmentRepository;
    private final UserService userService;
    private final WorkService workService;
    private final NotificationService notificationService;


    public AppointmentManagementServiceImpl(AppointmentRepository appointmentRepository,
                                            UserService userService,
                                            WorkService workService,
                                            NotificationService notificationService    
    ){
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
        this.workService = workService;
        this.notificationService = notificationService;

    }

    @Override
    public Appointment createNewAppointment(int workId, int providerId, int customerId, LocalDateTime start) {
        Work work = workService.getWorkById(workId);
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        Customer customer = userService.getCustomerById(customerId);
        Provider provider = userService.getProviderById(providerId);

        appointment.setCustomer(customer);
        appointment.setProvider(provider);
        appointment.setWork(work);
        appointment.setStart(start);
        appointment.setEnd(start.plusMinutes(work.getDuration()));

        appointmentRepository.save(appointment);
        notificationService.newNewAppointmentScheduledNotification(appointment, true);
        return appointment;
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Override
    public void deleteAppointmentById(int id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public Appointment getAppointmentById(int id) {
        return appointmentRepository.findById(id).orElseThrow(AppointmentNotFoundException::new);

    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getAppointmentByCustomerId(int customerId) {
        return appointmentRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Appointment> getAppointmentByProviderId(int providerId) {
        return appointmentRepository.findByProviderId(providerId);
    }

    @Override
    public List<Appointment> getAppointmentsByCustomerAtDay(int customerId, LocalDate day) {
        return appointmentRepository.findByCustomerIdWithStartInPeroid(customerId, day.atStartOfDay(), day.atStartOfDay().plusDays(1));
    }

    @Override
    public List<Appointment> getAppointmentsByProviderAtDay(int providerId, LocalDate day) {
        return appointmentRepository.findByProviderIdWithStartInPeroid(providerId, day.atStartOfDay(), day.atStartOfDay().plusDays(1));
    }
    
    @Override
    public List<Appointment> getConfirmedAppointmentsByCustomerId(int customerId) {
        return appointmentRepository.findConfirmedByCustomerId(customerId);
    }

    @Override
    public List<Appointment> getCanceledAppointmentsByCustomerIdForCurrentMonth(int customerId) {
        return appointmentRepository.findByCustomerIdCanceledAfterDate(customerId,
                LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay());
    }

    @Override
    public int getNumberOfCanceledAppointmentsForUser(int userId) {
        return appointmentRepository.findCanceledByUser(userId).size();
    }

     @Override
    public int getNumberOfScheduledAppointmentsForUser(int userId) {
        return appointmentRepository.findScheduledByUserId(userId).size();
    }
}
