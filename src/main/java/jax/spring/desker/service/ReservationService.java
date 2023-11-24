package jax.spring.desker.service;

public interface ReservationService {
    String RESPONSE_SUCCESS = "RESERVED";
    String RESPONSE_BOOKING_ALREADY_PRESENT = "BOOKING_FOR_SPECIFIED_DATE_ALREADY_PRESENT";
    String reserve();
}
