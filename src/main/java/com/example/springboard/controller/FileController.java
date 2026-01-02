package com.example.springboard.controller;

import com.example.springboard.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/*
이미지 업로드용 API
프론트에서 파일 업로드 -> URL 받음
게시글 작성시 imageUrl에 전달
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;
  
  // 업로드 /api/files/image
  @PostMapping("/image")
  public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) {
    // { "imageUrl": "/images/snow.png" } JSON 응답
    String imageUrl = fileService.saveImage(file);

    return Map.of("imageUrl", imageUrl);
  }

}
