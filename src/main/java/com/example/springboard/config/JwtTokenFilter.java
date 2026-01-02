package com.example.springboard.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// JWT 토큰 검사
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    // 요청 헤더에서 토큰 추출
    String token = resolveToken(request);
    
    // 토큰이 있고, 유효하면
    if(token != null && jwtTokenProvider.validateToken(token)) {
      // 정보를 꺼냄
      Long memberId = jwtTokenProvider.getMemberId(token);

      // 인증 객체 생성
      // controller에서 @Authentic....로 꺼내옴
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          memberId, // 로그인한 대상
          null, // 비밀번호. JWT로 이미 인증이 끝나서 null
          null // 권한
      );

      // SecurityContext 저장. controller에서 memberId 사용 가능
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }

    // 필터 연결
    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    // 헤더 Authorization 값 가져오기
    String bearerToken = request.getHeader("Authorization");

    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7); // "Bearer " 제거
    }

    return null;
  }

}
