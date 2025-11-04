package com.example.slabiak.appointmentscheduler.factory;


import org.springframework.stereotype.Component;

import com.example.slabiak.appointmentscheduler.model.enums.UserType;

@Component
public class UserFactoryProvider {
    private final ProviderFactory providerFactory;
    private final RetailCustomerFactory retailCustomerFactory;
    private final CorporateCustomerFactory corporateCustomerFactory;

    public UserFactoryProvider(ProviderFactory providerFactory, RetailCustomerFactory retailCustomerFactory, CorporateCustomerFactory corporateCustomerFactory){
        this.providerFactory = providerFactory;
        this.retailCustomerFactory = retailCustomerFactory;
        this.corporateCustomerFactory = corporateCustomerFactory;
    }

    public UserFactory getFactory(UserType userType) {
        switch (userType) {
            case PROVIDER:
                return providerFactory;
            case RETAIL_CUSTOMER:
                return retailCustomerFactory;
            case CORPORATE_CUSTOMER:
                return corporateCustomerFactory;
            default:
                throw new IllegalArgumentException("Invalid user type");
        }
    }
}
