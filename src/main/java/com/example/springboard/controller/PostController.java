package com.example.springboard.controller;

import com.example.springboard.dto.request.post.PostCreateRequest;
import com.example.springboard.dto.request.post.PostUpdateRequest;
import com.example.springboard.dto.response.common.PageResponse;
import com.example.springboard.dto.response.post.PostDetailResponse;
import com.example.springboard.dto.response.post.PostListResponse;
import com.example.springboard.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "게시글", description = "게시글 CURD API")
public class PostController {

  private final PostService postService;
  
  // 게시글 등록(회원만)
  @PostMapping
  @Operation(summary = "게시글 등록", description = "새로운 게시글 작성. 로그인한 사람만 작성할 수 있습니다.")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "게시글 작성 성공",
          content = @Content(schema = @Schema(implementation = PostDetailResponse.class))
      ),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 - 항목 누락 혹은 유효성 검증 실패"),
      @ApiResponse(responseCode = "401", description = "인증 필요 - 로그인 필요")
  })
  public ResponseEntity<PostDetailResponse> create(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "게시글 작성 요청 데이터",
          required = true,
          content = @Content(
              schema = @Schema(implementation = PostCreateRequest.class),
              examples = {
                  @ExampleObject(
                      name = "예시",
                      value = """
                          {
                            "title": "Swagger 적용",
                            "content" : "게시판 테스트 내용입니다.",
                            "imageUrl" : "/images/test.png"
                          }
                          """
                  )
              }
          )
      )
      @Valid @RequestBody PostCreateRequest request,
      @AuthenticationPrincipal Long memberId
  ) {
    return ResponseEntity.ok(postService.createPost(memberId, request));// 200 상태 코드 반환
  }

  // 게시글 목록 조회 + 검색 + 페이징
  // /api/posts?page=0&size=10&keyword=게시판
  @GetMapping
  public ResponseEntity<PageResponse<PostListResponse>> list(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) @Max(10) int size,
      @RequestParam(required = false) String keyword
  ) {
    return ResponseEntity.ok(postService.getPostList(page, size, keyword));
  }

  // 게시글 상세 조회 + 조회수 증가
  @GetMapping("/{id}")
  public ResponseEntity<PostDetailResponse> detail(@PathVariable Long id) {
    return ResponseEntity.ok(postService.getPostDetail(id));
  }

  // 게시글 수정(회원만)
  @PutMapping("/{id}")
  public ResponseEntity<PostDetailResponse> update(
      @PathVariable Long id,
      @RequestBody PostUpdateRequest request,
      @AuthenticationPrincipal Long memberId
      ) {
    return ResponseEntity.ok(postService.updatePost(memberId, id, request));
  }

  // 게시글 삭제(회원만)
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Long memberId) {
    postService.deletePost(memberId, id);
    return ResponseEntity.noContent().build();
  }

}
