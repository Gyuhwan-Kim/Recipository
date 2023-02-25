package com.example.recipository.config;

import com.example.recipository.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    public MvcConfiguration(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/user/contents/**", "/user/comments/**")
                .excludePathPatterns("/user/contents", "/user/comments");
    }

    @Value("/lib/upload/**")
    private String requestPath;
    @Value("file:///${file.directory}/")
    private String resourcePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(requestPath)
                .addResourceLocations(resourcePath);
    }
}
