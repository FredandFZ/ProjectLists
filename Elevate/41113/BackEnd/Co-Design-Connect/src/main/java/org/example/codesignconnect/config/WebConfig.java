package org.example.codesignconnect.config;

import org.example.codesignconnect.utils.HTTPSessionCheckInterceptor;
import org.example.codesignconnect.utils.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Autowired
    private HTTPSessionCheckInterceptor httpSessionCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patterns = new ArrayList<>();
        patterns.add("/login");
        patterns.add("/register");
        //patterns.add("/inviteCode");
        patterns.add("/password/resetCode");
        patterns.add("/password/update");
        patterns.add("/projects/all");
        patterns.add("/projects/search");
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns(patterns);
        registry.addInterceptor(httpSessionCheckInterceptor).addPathPatterns("/**").excludePathPatterns(patterns);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
