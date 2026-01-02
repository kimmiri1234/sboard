package com.example.springboard.dto.request.post;

import com.example.springboard.domain.Member;
import com.example.springboard.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

// 게시글 작성 요청 DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
  
  @NotBlank(message = "제목 필수")
  @Size(max = 200, message = "제목은 200자 이하로 작성")
  private String title;

  @NotBlank(message = "내용 필수")
  private String content;
  private String imageUrl;
  
  // 요청 DTO -> Post entity로 변환
  public Post toEntity(Member member) {
    return Post.builder()
        .member(member) // 작성자
        .title(title)
        .content(content)
        .imageUrl(imageUrl)
        .build();
  }

}
