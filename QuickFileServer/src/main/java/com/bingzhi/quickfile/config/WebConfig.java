package com.bingzhi.quickfile.config;

import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.web.servlet.config.annotation.CorsRegistry;
   import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

   @Configuration
   public class WebConfig implements WebMvcConfigurer {
       @Override
       public void addCorsMappings(CorsRegistry registry) {
           registry.addMapping("/**") // 匹配所有路径
                   .allowedOrigins("http://localhost:8080") // 允许的前端域名
                   .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                   .allowedHeaders("*") // 允许的请求头
                   .allowCredentials(true) // 允许发送凭证
                   .maxAge(3600); // 预检请求的缓存时间（1小时）
       }
   }
