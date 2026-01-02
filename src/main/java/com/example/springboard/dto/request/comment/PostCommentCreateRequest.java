package com.example.springboard.dto.request.comment;

import com.example.springboard.domain.Member;
import com.example.springboard.domain.Post;
import com.example.springboard.domain.PostComment;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

// 댓글 작성 요청 DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentCreateRequest {

  @NotBlank(message = "댓글 내용을 입력해야합니다.")
  private String content;

  // DTO -> Entity
  public PostComment toEntity(Post post, Member member) {
    return PostComment.builder()
        .post(post)
        .member(member)
        .content(content)
        .build();
  }
}



