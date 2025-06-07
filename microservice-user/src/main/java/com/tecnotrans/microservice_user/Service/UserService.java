package com.tecnotrans.microservice_user.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Repository.UserRepository;

@Service
public class UserService implements IUserService{

@Autowired
    UserRepository userRepository;

    @Override
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id){
        return userRepository.findById(id).get();
    }

    public Optional<User> getUserByIdOpt(Long id){
        return userRepository.findById(id);
    }

    @Override
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

    public boolean validateUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return true;
        } else {
            return false;
        }
    }

}
