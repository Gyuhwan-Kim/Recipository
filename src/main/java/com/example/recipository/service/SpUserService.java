package com.example.recipository.service;

import com.example.recipository.domain.SpAuthority;
import com.example.recipository.domain.SpUser;
import com.example.recipository.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class SpUserService implements UserDetailsService {
    private final UserRepository userRepository;

    public SpUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SpUser> spUser = userRepository.findSpUserByEmail(username);
        Set<SpAuthority>  authorities = spUser.get().getAuthorities();
        System.out.println(authorities);

        return spUser
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
