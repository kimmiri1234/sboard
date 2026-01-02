package com.example.springboard.dto.response.auth;

import com.example.springboard.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/*
로그인 **상태** 확인 + 사용자 정보 제공 DTO
- 페이지 새로고침, 회원 페이지, 게시글 작성자 확인용..

프론트에서 항상 JWT를 파싱하지 않고 현재 로그인한 사용자 정보를 
서버기준으로 확인해 
200 -> 로그인 유지
401 -> 비로그인
*/
@Getter
@AllArgsConstructor
@Builder
public class MyInfoResponse {

  private Long id;
  private  String email;
  private String nickname;

  // entity -> dto 변환
  public static MyInfoResponse from(Member member) {
    return MyInfoResponse.builder()
        .id(member.getId())
        .email(member.getEmail())
        .nickname(member.getNickname())
        .build();
  }
}



