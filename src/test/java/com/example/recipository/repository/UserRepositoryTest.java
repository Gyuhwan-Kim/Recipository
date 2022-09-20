package com.example.recipository.repository;

import com.example.recipository.model.entity.UserT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void insertDummy(){
        UserT user = new UserT();
        user.setNum(1);
        user.setEmail("dummy@gmail.com");
        user.setName("dummy");
        user.setPassword("1234");
        user.setCopy_contents("");
        user.setAuthor("no");

        userRepository.save(user);
    }
}
