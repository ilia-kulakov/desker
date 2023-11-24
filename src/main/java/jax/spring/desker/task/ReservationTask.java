package jax.spring.desker.task;

import jax.spring.desker.service.ReservationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static jax.spring.desker.service.ReservationService.RESPONSE_BOOKING_ALREADY_PRESENT;
import static jax.spring.desker.service.ReservationService.RESPONSE_SUCCESS;

@Component
public class ReservationTask {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationTask.class);
    @Autowired
    private ReservationService reservationService;
    private boolean active = true;

    @Scheduled(cron = "${jax.spring.desker.reservation.scheduler.cron}")
    public void reserve() {
        if(active) {
            LOG.info("Reservation Task is starting");
            String result = reservationService.reserve();
            active = !StringUtils.containsAny(result, RESPONSE_SUCCESS, RESPONSE_BOOKING_ALREADY_PRESENT);
        }
    }

    @Scheduled(cron = "${jax.spring.desker.reservation.scheduler.reset.cron}")
    public void reset() {
        LOG.info("ReservationTask reset");
        active = true;
    }
}
