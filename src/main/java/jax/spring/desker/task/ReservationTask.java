package jax.spring.desker.task;

import jax.spring.desker.service.ReservationService;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationTask {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationTask.class);
    private static final String RESULT_SUCCESS = "RESERVED";
    private static final String RESULT_BOOKING_ALREADY_PRESENT = "BOOKING_FOR_SPECIFIED_DATE_ALREADY_PRESENT";
    private boolean stopAutoReservation = false;

    @Autowired
    private ConfigurationBuilder<FileBasedConfiguration> configurationBuilder;

    @Autowired
    private ReservationService reservationService;

    @Scheduled(cron = "${jax.spring.desker.reservation.scheduler.cron}")
    public void reserve() {
        if(!stopAutoReservation) {
            LOG.info("Reservation Task is starting");
            String result = reservationService.reserve();
            stopAutoReservation = StringUtils.containsAny(result, RESULT_SUCCESS, RESULT_BOOKING_ALREADY_PRESENT);
        }
        if(!stopAutoReservation) {
            reservationService.nextSeat();
        }
    }

    @Scheduled(cron = "${jax.spring.desker.counter.reset.scheduler.cron}")
    public void resetSeatNo() {
        LOG.info("Seat counter reset");
        reservationService.firstSeat();
        stopAutoReservation = false;
    }
}
