package jax.spring.desker.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SettingsModel {
    String url;
    String assetId;
    String partyId;
    int shiftDays;
    String cookie;
}
