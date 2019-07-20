package com.example.jwtauth.dao;

import com.example.jwtauth.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

  MyUser findByUsername(String username);
}