package com.tecnotrans.microservice_user.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Repository.UserRepository;

@Service
public class UserService {

@Autowired
    UserRepository userRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).get();
    }

    public Optional<User> getUserByIdOpt(Long id){
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        userRepository.save(user);
        return user;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
