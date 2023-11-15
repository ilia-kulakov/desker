package jax.spring.desker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jax.spring.desker.model.Booking;
import jax.spring.desker.service.ReservationService;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

@Service
public class ReservationServiceImpl implements ReservationService {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationServiceImpl.class);
    private static final String RESERVATION_TYPE = "BOOK";
    private static final String RESERVATION_REASON = "WORKPLACE";
    private static final String RESERVATION_ASSET_TYPE = "PLACE";
    private static final String RESERVATION_PARTY_TYPE = "PERSON";
    private static final String RESERVATION_START_TIME = "00:00:00";
    private static final String RESERVATION_END_TIME = "23:59:59";
    private static final String RESERVATION_DATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    private ConfigurationBuilder<FileBasedConfiguration> configurationBuilder;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        objectMapper.setDateFormat(new SimpleDateFormat(RESERVATION_DATE_FORMAT));
    }

    @Override
    public void reserve() {
        try {
            LOG.info("Reserve desk");
            ImmutableConfiguration config = configurationBuilder.getConfiguration();
            String url = config.getString("reservation.url");
            String cookie = config.getString("reservation.cookie");
            String assetId = config.getString("reservation.asset.id");
            String partyId = config.getString("reservation.party.id");
            int shiftDays = config.getInt("reservation.shift.days");

            Calendar reservationDate = Calendar.getInstance();
            reservationDate.add(Calendar.DATE, shiftDays);

            Booking booking = buildBooking(assetId, partyId, reservationDate.getTime());
            String payload = objectMapper.writeValueAsString(booking);
            LOG.info("Request Payload " + payload);
            ResponseEntity<String>  response = post(url, cookie, payload);
            LOG.info("Response status code " + response.getStatusCode());
            if(response.getStatusCode().is2xxSuccessful()) {
                LOG.info("Response body:");
                LOG.info(response.getBody());
            }
        } catch (Exception e) {
            LOG.error("An error occurred during the reservation", e);
        }

    }

    private ResponseEntity<String> post(String url, String cookie, String payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", cookie);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(payload, headers),
                String.class);
    }

    private Booking buildBooking(String assetId, String partyId, Date date) {
        return Booking.builder()
                .reservation(
                        Booking.Reservation.builder()
                                .comment(Strings.EMPTY)
                                .type(RESERVATION_TYPE)
                                .reason(RESERVATION_REASON)
                                .start(Booking.Reservation.Moment.builder()
                                        .date(date)
                                        .time(RESERVATION_START_TIME)
                                        .build())
                                .end(Booking.Reservation.Moment.builder()
                                        .date(date)
                                        .time(RESERVATION_END_TIME)
                                        .build())
                                .assets(Collections.singletonList(Booking.Reservation.Asset.builder()
                                                .assetId(assetId)
                                                .assetType(RESERVATION_ASSET_TYPE)
                                                .partyId(partyId)
                                                .build()))
                                .parties(Collections.singletonList(Booking.Reservation.Party.builder()
                                                .partyType(RESERVATION_PARTY_TYPE)
                                                .partyId(partyId)
                                                .build()))
                                .build()
                )
                .flags(Booking.Flag.builder()
                        .cancelConflicting(false)
                        .checkin(false)
                        .build())
                .build();
    }
}
