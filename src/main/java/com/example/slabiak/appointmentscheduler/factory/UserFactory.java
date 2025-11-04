package com.example.slabiak.appointmentscheduler.factory;

import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.model.UserForm;

public interface UserFactory {
    User createUser(UserForm form);
    void updateUser(User user, UserForm updateData);
}
