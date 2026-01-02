package com.example.springboard.service.impl;

import com.example.springboard.domain.Member;
import com.example.springboard.domain.Post;
import com.example.springboard.dto.request.post.PostCreateRequest;
import com.example.springboard.dto.request.post.PostUpdateRequest;
import com.example.springboard.dto.response.common.PageResponse;
import com.example.springboard.dto.response.post.PostDetailResponse;
import com.example.springboard.dto.response.post.PostListResponse;
import com.example.springboard.repository.MemberRepository;
import com.example.springboard.repository.PostCommentRepository;
import com.example.springboard.repository.PostRepository;
import com.example.springboard.service.FileService;
import com.example.springboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

// entity 조회
// DTO <-> Entity 변환
@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final FileService fileService;
  private final MemberRepository memberRepository;
  private final PostCommentRepository postCommentRepository;

  // 로그인 여부
  private void loginCheck(Long memberId) {
    if(memberId == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
    }
  }

  // 게시글 작성
  @Override
  public PostDetailResponse createPost(Long memberId, PostCreateRequest request) {

    loginCheck(memberId);

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

    // 게시글 생성 DTO -> entity 변환
    Post post = request.toEntity(member);

    // 저장
    Post saved = postRepository.save(post);

    // 저장된 Entity -> Response DTO 변환
    return PostDetailResponse.from(saved);
  }

  // 게시글 목록 조회 + 페이징 + 검색
  @Override
  @Transactional(readOnly = true)
  public PageResponse<PostListResponse> getPostList(int page, int size, String keyword) {

    // 페이징 관련 작업
    // Pageable 생성 .of(페이지번호, 한 페이지 개수, 정렬(id 기준 내림차순))
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
    
    // 검색
    Page<Post> postPage = findPostPage(keyword, pageable);
    
    // postPage 엔티티 -> DTO 변환 -> 반환
    return PageResponse.from(postPage, PostListResponse::from);
  }

  private Page<Post> findPostPage(String keyword, Pageable pageable) {
    // 검색어가 없으면 전체 조회
    if(keyword == null || keyword.isBlank()) {
      return postRepository.findAll(pageable);
    }
    
    // 검색어 있으면 제목, 내용에서 검색
    return postRepository.findByTitleContainingIgnoreCaseOrContentContaining(
        keyword,
        keyword,
        pageable
    );
  }

  // 게시글 상세 조회 + 조회수 증가
  @Override
  public PostDetailResponse getPostDetail(Long id) {
    // id 조회
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "찾는 글 없음"));
    
    // 조회수
    post.increaseReadCount();

    // DTO 변환해 반환
    return PostDetailResponse.from(post);
  }

  // 게시글 수정
  @Override
  public PostDetailResponse updatePost(Long memberId, Long id, PostUpdateRequest request) {

    loginCheck(memberId);

    // id 조회
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "찾는 글 없음"));

    // 작성자와 수정자 비교
    if(!post.getMember().getId().equals(memberId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다.");
    }

    // 업데이트
    post.update(request.getTitle(), request.getContent(), request.getImageUrl());

    // 반환
    return PostDetailResponse.from(post);
  }

  // 게시글 삭제
  @Override
  public void deletePost(Long memberId, Long id) {

    loginCheck(memberId);

    // id 조회
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "찾는 글 없음"));

    if(!post.getMember().getId().equals(memberId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다.");
    }
    
    // 댓글 먼저 삭제
    postCommentRepository.deleteByPostId(id);
    fileService.deleteImage(post.getImageUrl());
    postRepository.delete(post);
  }
}

