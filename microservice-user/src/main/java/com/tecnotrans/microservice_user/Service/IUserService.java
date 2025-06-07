package com.tecnotrans.microservice_user.Service;

import java.util.List;

import com.tecnotrans.microservice_user.Model.User;

public interface IUserService {
    List<User> getUsers();

    User getUserById(Long id);
    
    User addUser(User user);
}
