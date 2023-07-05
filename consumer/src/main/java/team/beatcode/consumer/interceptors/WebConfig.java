package team.beatcode.consumer.interceptors;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.beatcode.consumer.feign.AuthFeign;

/**
 * 在此处配置的拦截器将先于Feign的Config中配置的拦截器生效
 * Feign的拦截器不能拦截并提前返回请求，还得看这边
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    @Lazy // 懒加载模式避免循环注入导致的灾难。不知道为什么，用就对了
    AuthFeign authFeign;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        // 主要判定交给注解
        registry.addInterceptor(new LoginInterceptor(authFeign)).addPathPatterns("/**");
    }
}
