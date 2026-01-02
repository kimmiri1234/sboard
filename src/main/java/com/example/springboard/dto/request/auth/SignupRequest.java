package com.example.springboard.dto.request.auth;

import com.example.springboard.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {

  @Email(message = "이메일 형식이 아닙니다.")
  @NotBlank(message = "이메일은 필수입니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Size(min = 4, max = 20, message = "비밀번호는 4~20자 사이입니다.")
  private String password;

  @NotBlank(message = "별명은 필수입니다.")
  @Size(min = 2, max = 10, message = "별명은 2~10자 사이입니다.")
  private String nickname;

  public Member toEntity(String encodedPassword) {
    return Member.builder()
        .email(email)
        .password(encodedPassword)
        .nickname(nickname)
        .build();
  }


}
