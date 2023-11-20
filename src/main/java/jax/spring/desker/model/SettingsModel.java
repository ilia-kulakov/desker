package jax.spring.desker.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SettingsModel {
    String url;
    List<String> assetIds;
    String partyId;
    int shiftDays;
    String cookie;
}
