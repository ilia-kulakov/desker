package jax.spring.desker.service;

import jax.spring.desker.model.SettingsModel;
import org.apache.commons.configuration2.ex.ConfigurationException;

public interface ReservationSettingsService {
    SettingsModel getSettings() throws ConfigurationException;
    void saveSettings(SettingsModel settings) throws ConfigurationException;
}
