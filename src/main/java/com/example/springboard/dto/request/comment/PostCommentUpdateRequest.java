package com.example.springboard.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

// 댓글 수정 요청 DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentUpdateRequest {

  @NotBlank(message = "댓글 내용을 입력해야합니다.")
  private String content;

}
