package com.example.recipository.service;

import com.example.recipository.model.entity.User;
import com.example.recipository.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
