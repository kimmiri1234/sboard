package com.example.springboard.dto.response.common;

// 페이징을 위한 공통 DTO
// 게시글, 회원 목록, 댓글 목록...

import com.example.springboard.domain.Post;
import com.example.springboard.dto.response.post.PostListResponse;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
  // PageResponse<PostListResponse>, PageResponse<MemberResponse>, PageResponse<CommentResponse>

  private List<T> content; // 리스트 데이터
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;

  // Page<Entity> -> PageResponse<DTO> 변환
  // from(Page<Entity 타입> page, Function<E, DTO 타입> mapper)
  public static <E, D> PageResponse<D> from(Page<E> page, Function<E, D> mapper) {
    // .map(mapper) -> Entity -> DTO 변환
    // PostListResponse :: from 변환 함수
    List<D> content = page.getContent().stream()
        .map(mapper)
        .toList();

    return PageResponse.<D>builder()
        .content(content)
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .build();
  }

  /* 게시글 리스트 전용
  public static PageResponse<PostListResponse> fromPostPage(Page<Post> page) {
    List<PostListResponse> content = new ArrayList<>();

    for(Post post : page.getContent()) {
      content.add(PostListResponse.from(post));
    }

    return PageResponse.<PostListResponse>builder()
        .content(content)
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .build();
  }
  */

}
/*
Page 객체 - JPA 제공
페이지와 관련된 정보를 담고 있는 객체
.getContent()  실제 데이터 리스트
.getTotalElement() 전체 갯수 
.getTotalPages() 전체 페이지 수
.getSize()  페이지 크기
.getNumber()  현재 페이지 번호
*/




