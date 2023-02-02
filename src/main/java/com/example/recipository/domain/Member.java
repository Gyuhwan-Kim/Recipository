package com.example.recipository.domain;

import com.example.recipository.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String name;
    private String password;
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SpAuthority> authorities;

    private boolean enabled;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Recipe> recipeList;
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Comment> commentList;

    public UserDto toDto(){
        return UserDto.builder()
                .email(email)
                .name(name)
                .build();
    }

    public void addAuthority(){
        SpAuthority spAuthority = SpAuthority.builder()
                .member(this)
                .authority("ROLE_USER")
                .build();
        HashSet<SpAuthority> authorities = new HashSet<>();
        authorities.add(spAuthority);

        this.authorities = authorities;
    }

    public SpUser toUserDetails(){
        return SpUser.builder()
                .userId(userId)
                .email(email)
                .name(name)
                .password(password)
                .authorities(authorities)
                .enabled(enabled)
                .build();
    }

    public void updateName(UserDto userDto){
        name = userDto.getName();
    }

    public void updatePassword(String password){
        this.password = password;
    }
}
