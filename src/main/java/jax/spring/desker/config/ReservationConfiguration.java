package jax.spring.desker.config;

import jax.spring.desker.error.RestTemplateResponseErrorHandler;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class ReservationConfiguration {

    @Value(value = "${jax.spring.desker.reservation.config.trigger.period.sec}")
    private int triggerPeriodSec;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "jax.spring.desker.reservation.config.location", matchIfMissing = false)
    public ConfigurationBuilder<FileBasedConfiguration> configurationBuilder(@Value("${jax.spring.desker.reservation.config.location}") String path)
            throws Exception {
        Parameters params = new Parameters();
        File propertiesFile = new File(new File(path).getCanonicalPath());

        ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.fileBased()
                                .setFile(propertiesFile));

        PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(),
                null, triggerPeriodSec, TimeUnit.SECONDS);
        trigger.start();

        return builder;
    }

}
