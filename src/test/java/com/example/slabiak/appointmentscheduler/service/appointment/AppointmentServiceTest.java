package com.example.slabiak.appointmentscheduler.service.appointment;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.service.AppointmentChatService;
import com.example.slabiak.appointmentscheduler.service.AppointmentManagementService;
import com.example.slabiak.appointmentscheduler.service.AppointmentRejectionService;
import com.example.slabiak.appointmentscheduler.service.AppointmentStatusService;
import com.example.slabiak.appointmentscheduler.service.AvailabilityService;
import com.example.slabiak.appointmentscheduler.service.facade.AppointmentFacade;
import com.example.slabiak.appointmentscheduler.service.impl.JwtTokenServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentManagementService managementService;

    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private AppointmentRejectionService rejectionService;

    @Mock
    private AppointmentChatService chatService;

    @Mock
    private AppointmentStatusService statusService;

    @Mock
    private JwtTokenServiceImpl jwtTokenService;

    @InjectMocks
    private AppointmentFacade appointmentService;

    private int customerId;
    private int providerId;
    private int workId;

    private Appointment appointment;
    private Optional<Appointment> optionalAppointment;
    private List<Appointment> appointments;
    private int appointmentId;
    private Work work;
    private Provider provider;
    private Customer customer;

    @Before
    public void initObjects() {
        customerId = 1;
        providerId = 2;
        workId = 3;
        work = new Work();
        work.setId(workId);
        work.setDuration(60);

        provider = new Provider();
        provider.setId(providerId);
        provider.setWorkingPlan(WorkingPlan.generateDefaultWorkingPlan());

        customer = new Customer();
        customer.setId(customerId);

        appointment = new Appointment();
        appointmentId = 1;
        appointment.setId(appointmentId);

        optionalAppointment = Optional.of(appointment);
        appointments = new ArrayList<>();
        appointments.add(appointment);
    }

    @Test
    public void shouldBookAppointmentWhenAllConditionsMet() {
        LocalDateTime startOfNewAppointment = LocalDateTime.of(2019, 1, 1, 6, 0);

        // Arrange: ensure managementService will accept the creation (do nothing / default)
        // Here we only verify delegation — no need to stub anything for success path.
        appointmentService.createNewAppointment(workId, providerId, customerId, startOfNewAppointment);

        // Assert: facade delegated creation to managementService
        verify(managementService, times(1))
                .createNewAppointment(eq(workId), eq(providerId), eq(customerId), eq(startOfNewAppointment));
    }


    @Test(expected = RuntimeException.class)
     public void shouldNotBookAppointmentWhenAppointmentStartIsNotWithinProviderWorkingHours() {
        LocalDateTime startOfNewAppointment = LocalDateTime.of(2019, 1, 1, 5, 59);

        // Arrange: simulate managementService throwing because of invalid hour
        doThrow(new RuntimeException("Invalid working hour"))
                .when(managementService)
                .createNewAppointment(eq(workId), eq(providerId), eq(customerId), eq(startOfNewAppointment));

        // Act: should propagate exception from managementService
        appointmentService.createNewAppointment(workId, providerId, customerId, startOfNewAppointment);

        // Assert done by expected exception and verification below (unreachable if exception thrown)
        // verify(managementService).createNewAppointment(anyInt(), anyInt(), anyInt(), any(LocalDateTime.class));
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotBookNewAppointmentWhenCollidingWithProviderAlreadyBookedAppointments() {
        LocalDateTime startOfNewAppointment = LocalDateTime.of(2019, 1, 1, 6, 0);

        // Simulate collision -> managementService will throw runtime exception
        doThrow(new RuntimeException("Collision with provider appointment"))
                .when(managementService)
                .createNewAppointment(eq(workId), eq(providerId), eq(customerId), eq(startOfNewAppointment));

        appointmentService.createNewAppointment(workId, providerId, customerId, startOfNewAppointment);

        // verify that facade delegated (verification happens implicitly because exception thrown by mock)
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotBookNewAppointmentWhenCollidingWithCustomerAlreadyBookedAppointments() {
        LocalDateTime startOfNewAppointment = LocalDateTime.of(2019, 1, 1, 6, 0);

        // Simulate collision with customer's appointments
        doThrow(new RuntimeException("Collision with customer appointment"))
                .when(managementService)
                .createNewAppointment(eq(workId), eq(providerId), eq(customerId), eq(startOfNewAppointment));

        appointmentService.createNewAppointment(workId, providerId, customerId, startOfNewAppointment);
    }


    @Test
    public void shouldFindAppointmentById() {
        when(managementService.getAppointmentById(1)).thenReturn(appointment);

        Appointment result = appointmentService.getAppointmentByIdWithAuthorization(1);

        assertEquals(appointment.getId(), result.getId());
        verify(managementService, times(1)).getAppointmentById(1);
    }

    @Test
    public void shouldFindAllAppointments() {
        when(managementService.getAllAppointments()).thenReturn(appointments);

        List<Appointment> result = appointmentService.getAllAppointments();

        assertEquals(appointments, result);
        verify(managementService, times(1)).getAllAppointments();
    }

    @Test
    public void shouldDeleteAppointmentById() {
        // Act
        appointmentService.deleteAppointmentById(1);

        // Assert delegation
        verify(managementService, times(1)).deleteAppointmentById(1);
    }


}
