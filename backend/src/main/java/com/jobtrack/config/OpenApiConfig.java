package com.jobtrack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jobTrackOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JobTrack REST API")
                        .description("Comprehensive RESTful API for JobTrack job application lifecycle tracking and metrics.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("JobTrack Development Team")
                                .email("support@jobtrack.local")));
    }
}
