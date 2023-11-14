package jax.spring.desker.error;

import jax.spring.desker.service.impl.ReservationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;


@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        LOG.error("An error occurred during request. Response Status Code: {}, Response Status Text: {}",
                httpResponse.getStatusCode(), httpResponse.getStatusText());
    }
}
