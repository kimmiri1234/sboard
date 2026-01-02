package com.example.springboard.dto.response.comment;

import com.example.springboard.domain.PostComment;
import com.example.springboard.dto.response.common.MemberResponse;
import lombok.*;

import java.time.LocalDateTime;

// 댓글 응답 DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentResponse {

  private Long id;
  private String content;
  private MemberResponse author;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // Entity -> DTO
  public static PostCommentResponse from(PostComment comment) {
    return PostCommentResponse.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .author(MemberResponse.from(comment.getMember()))
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .build();
  }
}





