package jax.spring.desker.task;

import jax.spring.desker.service.ReservationService;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationTask {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationTask.class);

    @Autowired
    private ConfigurationBuilder<FileBasedConfiguration> configurationBuilder;

    @Autowired
    private ReservationService reservationService;

    @Scheduled(cron = "${jax.spring.desker.reservation.scheduler.cron}")
    public void reserve() {
        LOG.info("Reservation Task is starting");
        reservationService.reserve();
    }

    //@Scheduled(cron = "*/1 * * * * *")
    public void recurrent() throws ConfigurationException {
        LOG.info("recurrent assetId {}", configurationBuilder.getConfiguration().getString("reservation.asset.id"));
    }
}
