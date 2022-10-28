package com.example.recipository.repository;

import com.example.recipository.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void insertDummy(){
        User user = new User();
        user.setNum(1);
        user.setEmail("dummy@gmail.com");
        user.setName("dummy");
        user.setPassword("1234");
        user.setCopy_contents("");
        user.setAuthor("no");

        userRepository.save(user);
    }

    @Test
    public void login(){
        User user = new User();
        user.setEmail("admin@recipository.com");

        User dbData = userRepository.getByEmail(user.getEmail());
        System.out.println(dbData);
    }
}
