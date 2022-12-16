package com.example.recipository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Email(message = "이메일 양식에 맞게 작성해주세요.")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9가-힁]{4,12}$", message = "4~12자로 입력해주세요.")
    private String name;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,20}$",
            message = "영문과 숫자를 합쳐 8자 이상 20자 이하로 입력해주세요.")
    private String password;
}
