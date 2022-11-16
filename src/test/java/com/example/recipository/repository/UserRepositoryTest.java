package com.example.recipository.repository;


import com.example.recipository.domain.SpUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void insertDummy(){
//        SpUser user = new SpUser();
//
////        user.setUserId(1L);
//        user.setEmail("dummy@gmail.com");
//        user.setName("dummy");
//        user.setPassword("dummy1234");
//
//        user.setEnabled(true);
//
//        userRepository.save(user);

    }

    @Test
    public void login(){
        SpUser user = new SpUser();
        user.setEmail("admin@recipository.com");

        SpUser dbData = userRepository.getByEmail(user.getEmail());
        System.out.println(dbData);
    }
}
