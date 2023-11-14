package jax.spring.desker;

import com.fasterxml.jackson.core.JsonProcessingException;
import jax.spring.desker.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class DeskerApplication implements CommandLineRunner {

	private final ReservationService reservationService;

	private static final Logger LOG = LoggerFactory.getLogger(DeskerApplication.class);

	private static final String ARG_RESERVE = "-reserve";

	@Autowired
	public DeskerApplication(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DeskerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		LOG.info("Command line run");
		if(Arrays.asList(args).contains(ARG_RESERVE)) {
			LOG.info("Find command line argument {}", ARG_RESERVE);
			reservationService.reserve();
		}
	}

}
