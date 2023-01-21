package com.example.recipository.dto;

import com.example.recipository.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @Email(message = "이메일 양식에 맞게 작성해주세요.")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9가-힁]{4,12}$", message = "4~12자로 입력해주세요.")
    private String name;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,20}$",
            message = "영문과 숫자를 합쳐 8자 이상 20자 이하로 입력해주세요.")
    private String password;
    private String oldPassword;

    public Member toEntity(){
        // 비밀번호 encoding
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPwd = encoder.encode(password);

        return Member.builder()
                .email(email)
                .name(name)
                .password(encodedPwd)
                .enabled(true)
                .build();
    }
}
