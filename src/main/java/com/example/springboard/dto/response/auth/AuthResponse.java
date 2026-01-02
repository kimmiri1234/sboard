package com.example.springboard.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
로그인 **성공** 여부 확인 DTO
로그인시 한 번만 사용
JWT 전달 목적
{
  "accessToken" : "fdgdGsckssr12354FgCsd",
  "tokenType" : "Bearer"
}
*/
@Getter
@AllArgsConstructor
public class AuthResponse {

  private String accessToken;
  private String tokenType;
}
