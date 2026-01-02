package com.example.springboard.repository;

import com.example.springboard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  // 로그인시 이메일로 회원을 찾을 때 사용
  Optional<Member> findByEmail(String email);

  // 회원가입시 이메일 중복 확인
  boolean existsByEmail(String email);
}
