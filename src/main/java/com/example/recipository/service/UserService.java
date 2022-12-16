package com.example.recipository.service;

import com.example.recipository.dto.UserDto;

public interface UserService {
    public boolean duplCheck(UserDto userDto);
    public boolean signin(UserDto userDto);
//    public boolean login(User user);
}
