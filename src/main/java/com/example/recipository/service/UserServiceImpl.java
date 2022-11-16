package com.example.recipository.service;

import com.example.recipository.domain.SpAuthority;
import com.example.recipository.domain.SpUser;
import com.example.recipository.domain.UserDto;
import com.example.recipository.model.entity.User;
import com.example.recipository.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean duplCheck(UserDto userDto) {
        boolean isChecked = false;

        if(userDto.getEmail() != null) {
            isChecked = userRepository.existsByEmail(userDto.getEmail());
        } else if(userDto.getName() != null) {
            isChecked = userRepository.existsByName(userDto.getName());
        }

        return isChecked;
    }

    @Transactional
    @Override
    public boolean signin(UserDto userDto) {
        // 비밀번호 encoding
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPwd = encoder.encode(userDto.getPassword());

        SpUser user = new SpUser();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(encodedPwd);
        user.setEnabled(true);

        if(userRepository.save(user) != null){
            addAuthority(user.getUserId(), "ROLE_USER");
            return true;
        }
        return false;
    }

    // 권한을 추가하는 method
    public void addAuthority(Long userId, String authority){
        userRepository.findById(userId).ifPresent(user -> {
            SpAuthority spAuthority = new SpAuthority(user.getUserId(), authority);
            if(user.getAuthorities() == null){
                HashSet<SpAuthority> authorities = new HashSet<>();
                authorities.add(spAuthority);
                user.setAuthorities(authorities);
                userRepository.save(user);
            } else if(!user.getAuthorities().contains(spAuthority)){
                HashSet<SpAuthority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(spAuthority);
                user.setAuthorities(authorities);
                userRepository.save(user);
            }
        });
    }

    @Override
    public boolean login(User user) {
        SpUser dbData = userRepository.getByEmail(user.getEmail());

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
