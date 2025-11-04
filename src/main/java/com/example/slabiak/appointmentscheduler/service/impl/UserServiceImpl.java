package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.dao.user.UserRepository;
import com.example.slabiak.appointmentscheduler.dao.user.customer.CorporateCustomerRepository;
import com.example.slabiak.appointmentscheduler.dao.user.customer.CustomerRepository;
import com.example.slabiak.appointmentscheduler.dao.user.customer.RetailCustomerRepository;
import com.example.slabiak.appointmentscheduler.dao.user.provider.ProviderRepository;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.factory.UserFactory;
import com.example.slabiak.appointmentscheduler.factory.UserFactoryProvider;
import com.example.slabiak.appointmentscheduler.model.ChangePasswordForm;
import com.example.slabiak.appointmentscheduler.model.UserForm;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final ProviderRepository providerRepository;
    private final CustomerRepository customerRepository;
    private final CorporateCustomerRepository corporateCustomerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, UserFactory> userFactories;

    public UserServiceImpl(
            ProviderRepository providerRepository, 
            CustomerRepository customerRepository, 
            CorporateCustomerRepository corporateCustomerRepository, 
            RetailCustomerRepository retailCustomerRepository, 
            UserRepository userRepository, 
            RoleRepository roleRepository, 
            PasswordEncoder passwordEncoder,
            UserFactoryProvider userFactoryProvider,
            List<UserFactory> factories        
    ) {
        this.providerRepository = providerRepository;
        this.customerRepository = customerRepository;
        this.corporateCustomerRepository = corporateCustomerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        
        this.userFactories = factories.stream().collect(Collectors.toMap(
            f -> f.getClass().getSimpleName().replace("Factory", "").toUpperCase(), 
            f -> f
            ));
    }

    private UserFactory getFactory(String type){
        UserFactory factory = userFactories.get(type.toUpperCase());
        if(factory == null){
            throw new IllegalArgumentException("No factory found for type:" + type);
        }
        return factory;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void saveNewUser(UserForm form) {
        UserFactory factory = getFactory(form.getUserType().name());
        User user = factory.createUser(form);
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("#updateData.id == principal.id or hasRole('ADMIN')")
    public void updateUserProfile(UserForm updateData) {
        User user = userRepository.getOne(updateData.getId());
        UserFactory factory = getFactory(updateData.getUserType().name());
        factory.updateUser(user, updateData);
        userRepository.save(user);
    }

    @Override
    public boolean userExists(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }

    @Override
    @PreAuthorize("#userId == principal.id")
    public User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User getUserByUsername(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    public List<User> getUsersByRoleName(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUserById(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    @PreAuthorize("#passwordChangeForm.id == principal.id")
    public void updateUserPassword(ChangePasswordForm passwordChangeForm) {
        User user = userRepository.getOne(passwordChangeForm.getId());
        user.setPassword(passwordEncoder.encode(passwordChangeForm.getPassword()));
        userRepository.save(user);
    }

    // PROVIDER

    @Override
    public Provider getProviderById(int providerId) {
        return providerRepository.findById(providerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }
    
    @Override
    public List<Provider> getProvidersWithRetailWorks() {
        return providerRepository.findAllWithRetailWorks();
    }

    @Override
    public List<Provider> getProvidersWithCorporateWorks() {
        return providerRepository.findAllWithCorporateWorks();
    }

    @Override
    public List<Provider> getProvidersByWork(Work work) {
        return providerRepository.findByWorks(work);
    }

    // CUSTOMERS

    @Override
    @PreAuthorize("#customerId == principal.id or hasRole('ADMIN')")
    public Customer getCustomerById(int customerId) {
        return customerRepository.getOne(customerId);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

}

