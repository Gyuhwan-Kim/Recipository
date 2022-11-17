package com.example.recipository.service;

import com.example.recipository.domain.SpUser;
import com.example.recipository.domain.UserDto;
import com.example.recipository.model.entity.User;

public interface UserService {
    public boolean duplCheck(UserDto userDto);
    public boolean signin(UserDto userDto);
//    public boolean login(User user);
}
