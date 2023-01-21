package com.example.recipository.domain;

import com.example.recipository.dto.UserDto;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpUser implements UserDetails {
    private Long userId;
    private String email;
    private String name;
    private String password;
    private Set<SpAuthority> authorities;

    private boolean enabled;


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    public Member toMember(){
        return Member.builder()
                .userId(userId)
                .email(email)
                .name(name)
                .password(password)
                .authorities(authorities)
                .enabled(enabled)
                .build();
    }

    public void updateName(String newName){
        this.name = newName;
    }
}
