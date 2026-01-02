package com.example.springboard.config;

import com.example.springboard.domain.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// 토큰 발급소: 생성(로그인 시), 검증(요청 시), 정보 추출(토큰에서)
@Component
public class JwtTokenProvider {
  
  // application.properties에서 주입
  // 비밀키
  private final String secret;
  // 만료시간
  private final long expiration;

  // 실제 암호화 키
  private SecretKey secretKey;

  public JwtTokenProvider(
      @Value("${jwt.secret}")  String secret,
      @Value("${jwt.expiration}")  long expiration
  ) {
    this.secret = secret;
    this.expiration = expiration;
  }

  // 객체 생성 후 초기화(문자열 비밀키를 실제 암호화 키로 변환)
  @PostConstruct
  public void init() {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  // 실제 사용
  // JWT 생성
  public String generateToken(Member member) {
    Date now = new Date(); // 현재 시간
    Date expiry = new Date(now.getTime() + expiration); // 1시간

    // jwts 메서드
    return Jwts.builder() // 객체(생성) 반환
        .subject(member.getEmail()) // 메인정보
        .claim("memberId", member.getId()) // 추가 정보
        .issuedAt(now) // 발급 시간
        .expiration(expiry) // 만료 시간
        .signWith(secretKey) // 비밀키로 서명
        .compact();// 문자열로 반환
  }

  // JWT 유효성 검증(서명, 만료 시간)
  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);

      return true;

    } catch (Exception e) {
      return false;// 위조되거나 만료 토큰
    }
  }
  
  // JWT에서 정보 추출
  // 토큰에서 memberId 추출
  public Long getMemberId(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("memberId", Long.class);
  }

  // 토큰에서 email 추출
  public String getEmail(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }
}
