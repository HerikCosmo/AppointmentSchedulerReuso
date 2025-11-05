package com.example.slabiak.appointmentscheduler.service.impl.event;

import com.example.slabiak.appointmentscheduler.entity.ExchangeRequest;

public class ExchangeAcceptedEvent {
    private final ExchangeRequest exchangeRequest;

    public ExchangeAcceptedEvent(ExchangeRequest exchangeRequest) {
        this.exchangeRequest = exchangeRequest;
    }

    public ExchangeRequest getExchangeRequest() {
        return exchangeRequest;
    }
}
