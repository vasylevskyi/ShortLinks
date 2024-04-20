package ua.goit.shortlinks.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi authOpenApi() {
        return GroupedOpenApi.builder()
                .group("Auth")
                .pathsToMatch("/auth/***")
                .build();
    }
    @Bean
    public GroupedOpenApi LinkOpenApi() {
        return GroupedOpenApi.builder()
                .group("LinkController")
                .pathsToMatch("/links/**")
                .pathsToExclude("/links/search/**")
                .build();
    }
    @Bean
    public GroupedOpenApi RedirectOpenApi() {
        return GroupedOpenApi.builder()
                .group("RedirectController")
                .pathsToMatch("/{shortLink}")
                .build();
    }


}



