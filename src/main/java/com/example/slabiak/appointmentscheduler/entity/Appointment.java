package com.example.slabiak.appointmentscheduler.entity;

import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.model.AppointmentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "appointments")
@JsonSerialize(using = AppointmentSerializer.class)
public class Appointment extends BaseEntity implements Comparable<Appointment> {

    @Column(name = "start")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "end")
    private LocalDateTime end;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @OneToOne
    @JoinColumn(name = "id_canceler")
    private User canceler;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "id_provider")
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "id_work")
    private Work work;

    @OneToMany(mappedBy = "appointment")
    private List<ChatMessage> chatMessages;

    @ManyToOne
    @JoinColumn(name = "id_invoice")
    private Invoice invoice;

    @OneToOne(mappedBy = "requested", cascade = {CascadeType.ALL})
    private ExchangeRequest exchangeRequest;

    public Appointment() {
    }

    public Appointment(LocalDateTime start, LocalDateTime end, Customer customer, Provider provider, Work work) {
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.provider = provider;
        this.work = work;
    }

    @Override
    public int compareTo(Appointment o) {
        return this.getStart().compareTo(o.getStart());
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public List<ChatMessage> getChatMessages() {
        Collections.sort(chatMessages);
        return chatMessages;
    }

    public User getCanceler() {
        return canceler;
    }

    public void setCanceler(User canceler) {
        this.canceler = canceler;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public ExchangeRequest getExchangeRequest() {
        return exchangeRequest;
    }

    public void setExchangeRequest(ExchangeRequest exchangeRequest) {
        this.exchangeRequest = exchangeRequest;
    }

    public boolean isOwnedByCustomer(int customerId){
        return this.customer != null && this.customer.getId() == customerId;
    }

    public boolean isOwnedByProvider(int providerId){
        return this.provider != null && this.provider.getId() == providerId;
    }

    public boolean isAlreadyCanceled(){
        return this.status == AppointmentStatus.CANCELED;
    }

    public boolean isPendingRejection(){
        return this.exchangeRequest != null && !this.isAlreadyCanceled();
    }

    public void cancel(User canceler){
        this.status = AppointmentStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
        this.canceler = canceler;
    }

    public boolean isRejectionRequested(){
        return this.status == AppointmentStatus.REJECTION_REQUESTED;
    }

    public void setRejectionRequested(boolean requested){
        if(requested){
            this.status = AppointmentStatus.REJECTION_REQUESTED;
        } else if(this.status == AppointmentStatus.REJECTION_REQUESTED){
            this.status = AppointmentStatus.FINISHED;
        }
    }
}
