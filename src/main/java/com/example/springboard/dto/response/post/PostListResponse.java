package com.example.springboard.dto.response.post;

import com.example.springboard.domain.Post;
import com.example.springboard.dto.response.common.MemberResponse;
import lombok.*;

import java.time.LocalDateTime;

// 게시글 리스트 응답 DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListResponse {

  private Long id;
  private String title; // 제목
  private MemberResponse author;// 작성자(id, nickname)
  private Integer readCount; // 조회 수
  private LocalDateTime createAt; // 작성일

  // Post entity -> 리스트 DTO
  public static PostListResponse from(Post post) {
    return PostListResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .author(MemberResponse.from(post.getMember()))// 작성자 매핑
        .readCount(post.getReadCount())
        .createAt(post.getCreateAt())
        .build();
  }
}






