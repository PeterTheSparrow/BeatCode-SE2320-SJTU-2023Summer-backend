package team.beatcode.judge.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class AppConfig {

    @Bean
    @Qualifier("customResourceLoader")
    public ResourceLoader customResourceLoader() {
        return new DefaultResourceLoader();
    }
}