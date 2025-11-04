package com.example.slabiak.appointmentscheduler.factory;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.customer.CorporateCustomer;
import com.example.slabiak.appointmentscheduler.model.UserForm;

@Component
public class CorporateCustomerFactory implements UserFactory{
    
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CorporateCustomerFactory(RoleRepository roleRepository,PasswordEncoder passwordEncoder){
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(UserForm form) {
        return new CorporateCustomer(
            form,
            passwordEncoder.encode(form.getPassword()),
            getRolesForCorporateCustomer()
        );
    }

    @Override
    public void updateUser(User user, UserForm updateData) {
        ((CorporateCustomer) user).update(updateData);
    }

    private Collection<Role> getRolesForCorporateCustomer(){
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_CUSTOMER_CORPORATE"));
        roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
        return roles;
    }
}
