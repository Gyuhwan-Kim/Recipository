package com.example.recipository.service;

import com.example.recipository.domain.Member;
import com.example.recipository.dto.UserDto;

public interface UserService {
    boolean duplCheck(UserDto userDto);
    boolean signin(UserDto userDto);
//    boolean login(User user);
    UserDto getProfile(String email);
    boolean updateProfile(UserDto userDto);
    boolean updatePassword(UserDto userDto);
    boolean exit(Member member);
}
