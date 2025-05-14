package com.pulse.config;

import com.pulse.fhir.config.FhirJacksonConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FhirJacksonConfig fhirJacksonConfig;

    public WebConfig(FhirJacksonConfig fhirJacksonConfig) {
        this.fhirJacksonConfig = fhirJacksonConfig;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Add FHIR message converter first
        converters.add(0, fhirJacksonConfig.fhirMessageConverter());
    }
}