package com.example.slabiak.appointmentscheduler.service.impl.event;

import com.example.slabiak.appointmentscheduler.entity.ExchangeRequest;

public class ExchangeRejectedEvent {
    private final ExchangeRequest exchangeRequest;

    public ExchangeRejectedEvent(ExchangeRequest exchangeRequest) {
        this.exchangeRequest = exchangeRequest;
    }

    public ExchangeRequest getExchangeRequest() {
        return exchangeRequest;
    }
}
