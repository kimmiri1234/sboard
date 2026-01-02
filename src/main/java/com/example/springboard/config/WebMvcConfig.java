package com.example.springboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
    String uploads = "file:" + uploadPath.toString() + "/";

    // /images/xxx.png -> 로컬 uploadDir 폴더에서 파일을 찾을 수 있게 매핑
    registry.addResourceHandler("/images/**")
        .addResourceLocations(uploads);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins(
            "http://localhost:5173/",
            "https://tboard-flax.vercel.app"
        )
        .allowedHeaders("*")
        .allowedMethods("*")
        .allowCredentials(true);
  }
}
