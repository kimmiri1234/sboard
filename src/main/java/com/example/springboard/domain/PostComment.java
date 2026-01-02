package com.example.springboard.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  // 회원 1 : 댓글 N
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  // 댓글 N개 : 1개 게시글
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @Column(name = "content", nullable = false, length = 1000)
  private String content;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private  LocalDateTime updatedAt;
  
  // 댓글 수정 메서드
  public void updateContent(String content) {
    this.content = content;
  }

}
