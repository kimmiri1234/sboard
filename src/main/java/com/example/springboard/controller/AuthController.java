package com.example.springboard.controller;

import com.example.springboard.dto.request.auth.LoginRequest;
import com.example.springboard.dto.request.auth.SignupRequest;
import com.example.springboard.dto.response.auth.AuthResponse;
import com.example.springboard.dto.response.auth.MyInfoResponse;
import com.example.springboard.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 회원가입, 로그인, 내정보
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
    authService.signup(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();// 201. POST
  }

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  // 내정보
  @GetMapping("/myinfo")
  public ResponseEntity<MyInfoResponse> myinfo(@AuthenticationPrincipal Long memberId) {
    return ResponseEntity.ok(authService.myinfo(memberId));
  }

}





