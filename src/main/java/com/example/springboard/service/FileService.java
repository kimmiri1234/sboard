package com.example.springboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

  @Value("${file.upload-dir}")
  private String uploadDir;
  
  // 저장
  public String saveImage(MultipartFile file) {

    // 파일 존재에 대한 검증
    if(file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NO_CONTENT, "업로드 파일 없음");
    }

    try {

      // 업로드(절대) 경로 생성
      Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
      log.info("uploadPath={}", uploadPath);

      // 폴더가 없으면 생성
      if(!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      // 확장자 추출 .png, .jpg ..등
      String originalName = file.getOriginalFilename();
      String ext = "";
      int idx = originalName.lastIndexOf('.');// 문자열에서 해당 글자가 마지막으로 나온 위치를 찾음. 숫자로 반환
      if(idx != -1) {
        ext = originalName.substring(idx);// idx 위치부터 끝까지 자름
      }

      // 새 파일명: uuid + 확장자
      String newName = UUID.randomUUID().toString() + ext;

      // uploadPath: **/**/uploads/images
      // newName:1231fsdfsdsd321fsds.png
      // uploadPath + newName ->  **/**/uploads/images/1231fsdfsdsd321fsds.png
      Path target = uploadPath.resolve(newName);
      
      // 실제 저장. Path -> File로 변경(저장)
      file.transferTo(target.toFile());

      // 프론트에서 사용할 URL 반환. {} json 값으로 들어갈 url
      return "/images/" + newName;

    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 실패", e);
    }


    
  }
  
  // 삭제
  public void deleteImage(String imageUrl) {
    if(imageUrl == null || imageUrl.isBlank()) return;

    //  "/images/파일명" -> 파일명
    String fileName = imageUrl.replace("/images/","");

    Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
    Path target = uploadPath.resolve(fileName).normalize();

    try {
      Files.deleteIfExists(target);
      
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제 중 오류 발생", e);
    }
  }

}
