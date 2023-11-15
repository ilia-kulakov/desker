package jax.spring.desker.config;

import jax.spring.desker.error.RestTemplateResponseErrorHandler;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Configuration
public class ReservationConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "jax.spring.desker.reservation.config.location", matchIfMissing = false)
    public FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder(
            @Value("${jax.spring.desker.reservation.config.location}") String path) throws Exception {
        return new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(new Parameters()
                        .fileBased()
                        .setFile(new File(path)));
    }

}
