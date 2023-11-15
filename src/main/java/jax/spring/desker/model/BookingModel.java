package jax.spring.desker.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
@Builder
public class BookingModel {

    Reservation reservation;
    Flag flags;

    @Value
    @Builder
    public static class Reservation {
        String comment;
        String type;
        String reason;
        Moment start;
        Moment end;
        List<Asset> assets;
        List<Party> parties;

        @Value
        @Builder
        public static class Moment {
            Date date;
            String time;
        }

        @Value
        @Builder
        public static class Asset {
            String assetId;
            String assetType;
            String partyId;
        }

        @Value
        @Builder
        public static class Party {
            String partyId;
            String partyType;
        }
    }

    @Value
    @Builder
    public static class Flag {
        boolean cancelConflicting;
        boolean checkin;
    }
}
