package com.example.recipository.service;

import com.example.recipository.model.entity.User;
import com.example.recipository.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean duplCheck(User user) {
        boolean isChecked = false;

        if(user.getEmail() != null) {
            isChecked = userRepository.existsByEmail(user.getEmail());
        } else if(user.getName() != null) {
            isChecked = userRepository.existsByName(user.getName());
        }

        return isChecked;
    }

    @Override
    public boolean signin(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPwd = encoder.encode(user.getPassword());

        user.setPassword(encodedPwd);

        if(userRepository.save(user) != null){
            return true;
        }
        return false;
    }

    @Override
    public boolean login(User user) {
        User dbData = userRepository.getByEmail(user.getEmail());

        // DB에 data가 있는 경우
        if(dbData != null) {
            boolean checkPwd = BCrypt.checkpw(user.getPassword(), dbData.getPassword());

            // 비밀 번호가 일치하는 경우
            if(checkPwd){
                return true;
            }
        }

        return false;
    }


}
