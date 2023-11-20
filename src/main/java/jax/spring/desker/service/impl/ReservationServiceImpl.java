package jax.spring.desker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jax.spring.desker.model.BookingModel;
import jax.spring.desker.model.SettingsModel;
import jax.spring.desker.service.ReservationService;
import jax.spring.desker.service.ReservationSettingsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
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
    private ReservationSettingsService reservationSettingsService;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private int seatNo;

    @PostConstruct
    public void init() {
        objectMapper.setDateFormat(new SimpleDateFormat(RESERVATION_DATE_FORMAT));
        seatNo = 0;
    }

    private String reserve(@NonNull String url,
                          @NonNull String partyId,
                          @NonNull String assertId,
                          @NonNull Calendar date,
                          @NonNull String cookie) {
        try {
            LOG.info("Reserve desk");
            BookingModel bookingModel = buildBooking(assertId, partyId, date.getTime());
            String payload = objectMapper.writeValueAsString(bookingModel);
            LOG.info("Request Payload " + payload);
            ResponseEntity<String>  response = post(url, cookie, payload);
            LOG.info("Response status code " + response.getStatusCode());
            if(StringUtils.isNoneBlank(response.getBody())) {
                LOG.info("Response body:");
                LOG.info(response.getBody());
                return response.getBody();
            }
        } catch (Exception e) {
            LOG.error("An error occurred during the reservation", e);
        }

        return "{'error': 'An error occurred during the reservation'}";
    }

    @Override
    public String reserve() {
        try {
            SettingsModel settings = reservationSettingsService.getSettings();
            Calendar reservationDate = Calendar.getInstance();
            reservationDate.add(Calendar.DATE, settings.getShiftDays());
            int seatIndex = seatNo % settings.getAssetIds().size();

            return reserve(
                    settings.getUrl(),
                    settings.getPartyId(),
                    settings.getAssetIds().get(seatIndex),
                    reservationDate,
                    settings.getCookie());
        } catch (Exception e) {
            LOG.error("An error occurred during the reservation", e);
        }

        return "{'error': 'An error occurred during the reservation'}";
    }

    public void nextSeat() {
        seatNo++;
    }

    public void firstSeat() {
        seatNo = 0;
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

    private BookingModel buildBooking(String assetId, String partyId, Date date) {
        return BookingModel.builder()
                .reservation(
                        BookingModel.Reservation.builder()
                                .comment(Strings.EMPTY)
                                .type(RESERVATION_TYPE)
                                .reason(RESERVATION_REASON)
                                .start(BookingModel.Reservation.Moment.builder()
                                        .date(date)
                                        .time(RESERVATION_START_TIME)
                                        .build())
                                .end(BookingModel.Reservation.Moment.builder()
                                        .date(date)
                                        .time(RESERVATION_END_TIME)
                                        .build())
                                .assets(Collections.singletonList(BookingModel.Reservation.Asset.builder()
                                                .assetId(assetId)
                                                .assetType(RESERVATION_ASSET_TYPE)
                                                .partyId(partyId)
                                                .build()))
                                .parties(Collections.singletonList(BookingModel.Reservation.Party.builder()
                                                .partyType(RESERVATION_PARTY_TYPE)
                                                .partyId(partyId)
                                                .build()))
                                .build()
                )
                .flags(BookingModel.Flag.builder()
                        .cancelConflicting(false)
                        .checkin(false)
                        .build())
                .build();
    }
}
