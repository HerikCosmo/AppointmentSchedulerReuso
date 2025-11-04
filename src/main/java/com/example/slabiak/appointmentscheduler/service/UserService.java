package com.example.slabiak.appointmentscheduler.service;


import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.model.ChangePasswordForm;
import com.example.slabiak.appointmentscheduler.model.UserForm;

import java.util.List;

public interface UserService {
    /*
     * User
     * */
    boolean userExists(String userName);
    User getUserById(int userId);
    User getUserByUsername(String userName);
    List<User> getUsersByRoleName(String roleName);
    List<User> getAllUsers();
    void deleteUserById(int userId);
    void updateUserPassword(ChangePasswordForm passwordChangeForm);

    /*
     * Providers
     * */
    Provider getProviderById(int providerId);
    List<Provider> getProvidersWithRetailWorks();
    List<Provider> getProvidersWithCorporateWorks();
    List<Provider> getProvidersByWork(Work work);
    List<Provider> getAllProviders();

    /*
     * Customers
     * */
    Customer getCustomerById(int customerId);
    List<Customer> getAllCustomers();

    // factory methods
    void saveNewUser(UserForm form);
    void updateUserProfile(UserForm updateData);


}

