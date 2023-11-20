package jax.spring.desker.error;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        LOG.error("An error occurred during request. Response Status Code: {}, Response Status Text: {}, Body: {}",
                httpResponse.getStatusCode(),
                httpResponse.getStatusText(),
                IOUtils.toString(httpResponse.getBody(), StandardCharsets.UTF_8));
    }
}
