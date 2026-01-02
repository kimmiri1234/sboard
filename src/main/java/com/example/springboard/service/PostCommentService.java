package com.example.springboard.service;

import com.example.springboard.dto.request.comment.PostCommentCreateRequest;
import com.example.springboard.dto.request.comment.PostCommentUpdateRequest;
import com.example.springboard.dto.response.comment.PostCommentResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface PostCommentService {

  // 조회
  List<PostCommentResponse> getComments(Long postId);

  // 작성
  PostCommentResponse create(Long memberId, Long postId, PostCommentCreateRequest request);

  // 수정
  PostCommentResponse update(Long memberId, Long commentId, PostCommentUpdateRequest request);

  // 삭제
  void delete(Long memberId, Long commentId);
}
