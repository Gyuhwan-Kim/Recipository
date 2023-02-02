package com.example.recipository.repository;


import com.example.recipository.domain.Member;
import com.example.recipository.domain.Recipe;
import com.example.recipository.domain.SpUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;

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

//    @Test
//    public void login(){
//        SpUser user = new SpUser();
//        user.setEmail("admin@recipository.com");
//
//        SpUser dbData = userRepository.getByEmail(user.getEmail());
//        System.out.println(dbData);
//    }

    @Test
    @Transactional
    public void test2(){
        Member member = userRepository.getMemberByEmail("test3@test.com");
        List<Recipe> recipeList = member.getRecipeList();
        recipeList.clear();

        Member member2 = userRepository.getReferenceById(0L);

        int result = commentRepository.updateAllByMember(member, member2);
        System.out.println(result);

        userRepository.delete(member);

        System.out.println("test over");
    }
}
