package com.example.springboard.repository;

import com.example.springboard.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  // 제목과 내용에 키워드 포함 검색
  Page<Post> findByTitleContainingIgnoreCaseOrContentContaining(
      String title,
      String content,
      Pageable pageable
      // 첫번째 페이지, 한페이지에 10개 데이터, 아이디 기준 정렬, 내림차순
      // Pageable.of(0, 10, Sort.by("id").descending())
  );
}
/*
매서드 이름 규칙에 맞춰 만들면 자동으로 쿼리를 만들어 줌
findBy + 필드명 + 조건[ + 연결조건 + 필드명 + 조건 ...]
findBy 검색
Title 엔티티의 title 필드
Containing 특정 문자열이 포함된 데이터 검색
IgnoreCase 대소문자 구문없음
Or 혹은
Content 엔티티의 content 필드
*/