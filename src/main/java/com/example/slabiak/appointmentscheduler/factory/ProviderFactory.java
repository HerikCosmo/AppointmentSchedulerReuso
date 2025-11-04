package com.example.slabiak.appointmentscheduler.factory;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.model.UserForm;

import java.util.Collection;
import java.util.HashSet;;

@Component
public class ProviderFactory implements UserFactory {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public ProviderFactory(RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(UserForm form) {
        WorkingPlan workingPlan = WorkingPlan.generateDefaultWorkingPlan();
        return new Provider(
            form,
            passwordEncoder.encode(form.getPassword()),
            getRolesForProvider(),
            workingPlan
        );
    }

    @Override
    public void updateUser(User user, UserForm updateData) {
        ((Provider) user).update(updateData);
    }

    private Collection<Role> getRolesForProvider(){
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_PROVIDER"));
        return roles;
    }
}
