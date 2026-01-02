package com.example.springboard.repository;

import com.example.springboard.domain.Post;
import com.example.springboard.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

  // 특정 게시글의 댓글을 작성일 기준으로 오름차순 정렬
  List<PostComment> findByPostOrderByCreatedAtAsc(Post post);

  void deleteByPostId(Long postId);
}
