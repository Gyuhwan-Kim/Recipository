package com.example.recipository.service;

import com.example.recipository.domain.Member;
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
        Optional<Member> member = userRepository.findMemberByEmail(username);
        if(member.isEmpty()){
            throw new UsernameNotFoundException(username);
        }
        Set<SpAuthority> authorities = member.get().getAuthorities();
        System.out.println(authorities.size());

        SpUser spUser = member.get().toUserDetails();

        return spUser;
    }
}
