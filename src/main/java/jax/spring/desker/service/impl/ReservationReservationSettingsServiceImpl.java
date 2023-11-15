package jax.spring.desker.service.impl;

import jax.spring.desker.model.SettingsModel;
import jax.spring.desker.service.ReservationSettingsService;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ReservationReservationSettingsServiceImpl implements ReservationSettingsService {

    private static final String URL_PROPERTY_NAME = "reservation.url";
    private static final String ASSET_ID_PROPERTY_NAME = "reservation.asset.id";
    private static final String PARTY_ID_PROPERTY_NAME = "reservation.party.id";
    private static final String SHIFT_DAYS_PROPERTY_NAME = "reservation.shift.days";
    private static final String COOKIE_PROPERTY_NAME = "reservation.cookie";

    @Autowired
    private FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder;

    public SettingsModel getSettings() throws ConfigurationException {
        FileBasedConfiguration config = configurationBuilder.getConfiguration();
        return SettingsModel.builder()
                .url(config.getString(URL_PROPERTY_NAME))
                .assetId(config.getString(ASSET_ID_PROPERTY_NAME))
                .partyId(config.getString(PARTY_ID_PROPERTY_NAME))
                .shiftDays(config.getInt(SHIFT_DAYS_PROPERTY_NAME))
                .cookie(config.getString(COOKIE_PROPERTY_NAME))
                .build();
    }


    public void saveSettings(@NonNull SettingsModel settings) throws ConfigurationException {
        FileBasedConfiguration config = configurationBuilder.getConfiguration();
        config.setProperty(URL_PROPERTY_NAME, settings.getUrl());
        config.setProperty(ASSET_ID_PROPERTY_NAME, settings.getAssetId());
        config.setProperty(PARTY_ID_PROPERTY_NAME, settings.getPartyId());
        config.setProperty(SHIFT_DAYS_PROPERTY_NAME, settings.getShiftDays());
        config.setProperty(COOKIE_PROPERTY_NAME, settings.getCookie());
        configurationBuilder.save();
    }
}
