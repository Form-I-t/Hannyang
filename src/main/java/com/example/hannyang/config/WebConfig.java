package com.example.hannyang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대한 CORS 요청을 허용합니다.
                .allowedOrigins("http://localhost:3002") // 모든 출처(origin)에서의 요청을 허용합니다.
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드를 지정합니다.
                .allowedHeaders("*") // 모든 헤더를 허용합니다.
                .allowCredentials(true) // 쿠키 등의 인증 정보를 허용합니다.
                .maxAge(3600); // pre-flight 요청의 캐시 시간을 지정합니다.
    }
}
