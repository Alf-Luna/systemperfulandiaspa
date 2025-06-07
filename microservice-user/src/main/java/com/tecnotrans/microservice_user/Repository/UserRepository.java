package com.tecnotrans.microservice_user.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecnotrans.microservice_user.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
