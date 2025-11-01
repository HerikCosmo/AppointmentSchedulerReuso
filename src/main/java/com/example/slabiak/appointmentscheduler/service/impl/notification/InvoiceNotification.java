package com.example.slabiak.appointmentscheduler.service.impl.notification;

import com.example.slabiak.appointmentscheduler.entity.Invoice;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

public class InvoiceNotification extends NotificationTemplate {
    private final Invoice invoice;

    public InvoiceNotification(Invoice invoice, NotificationService notificationService, EmailService emailService, boolean mailingEnabled) {
        super(notificationService, emailService, mailingEnabled);
        this.invoice = invoice;
    }

    @Override
    protected String getTitle() {
        return "New invoice";
    }

    @Override
    protected String getMessage() {
        return "New invoice has been issued for you";
    }

    @Override
    protected String getUrl() {
        return "/invoices/" + invoice.getId();
    }

    @Override
    protected User getUser() {
        return invoice.getAppointments().get(0).getCustomer();
    }

    @Override
    protected void sendEmailNotification() {
        emailService.sendInvoice(invoice);
    }
}
