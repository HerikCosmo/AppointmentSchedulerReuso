package com.example.slabiak.appointmentscheduler.factory;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.model.UserForm;

@Component
public class RetailCustomerFactory implements UserFactory {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RetailCustomerFactory(RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User createUser(UserForm form) {
        return new RetailCustomer(
            form,
            passwordEncoder.encode(form.getPassword()),
            getRolesForRetailCustomer()
        );
    }

    @Override
    public void updateUser(User user, UserForm updateData) {
        ((RetailCustomer) user).update(updateData);
        
    }

    private Collection<Role> getRolesForRetailCustomer(){
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_CUSTOMER_RETIAIL"));
        roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
        return roles;
    }

}
