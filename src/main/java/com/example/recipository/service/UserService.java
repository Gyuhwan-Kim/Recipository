package com.example.recipository.service;

import com.example.recipository.model.entity.User;

public interface UserService {
    public boolean duplCheck(User user);
    public boolean signin(User user);
}
