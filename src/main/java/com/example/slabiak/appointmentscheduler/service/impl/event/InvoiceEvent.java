package com.example.slabiak.appointmentscheduler.service.impl.event;

import com.example.slabiak.appointmentscheduler.entity.Invoice;

public class InvoiceEvent {
    private Invoice invoice;

    public InvoiceEvent(Invoice invoice) {
        this.invoice = invoice;
    }

    public Invoice getInvoice() {
        return invoice;
    }
}
