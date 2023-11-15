package jax.spring.desker.controller;

import jax.spring.desker.model.SettingsModel;
import jax.spring.desker.service.ReservationSettingsService;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationSettingsService reservationSettingsService;

    @RequestMapping(
            value = "/settings",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public @ResponseBody SettingsModel getSettings() throws ConfigurationException {
        return reservationSettingsService.getSettings();
    }

    @RequestMapping(
            value = "/settings",
            method = RequestMethod.POST,
            consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody SettingsModel updateSettings(@RequestBody SettingsModel settings) throws ConfigurationException {
        reservationSettingsService.saveSettings(settings);
        return reservationSettingsService.getSettings();
    }
}
