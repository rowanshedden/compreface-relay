package aero.sita.compreface.handlers;

import aero.sita.compreface.exception.ServiceException;
import aero.sita.compreface.models.dto.Error;
import aero.sita.compreface.models.dto.RawHttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class RestInterfaceHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private HttpHeaders headers;

    @Value("${app.feeder.recognition.apikey}")
    private String apikey;

    @Autowired
    RestTemplate restTemplate;

    @PostConstruct
    void postConstruct() {
        headers = new HttpHeaders();
        headers.add("x-api-key", apikey);
    }

    /**
     * Call the CompreFace API
     *
     * @param domain String
     * @param path String
     * @param httpMethod HttpMethod
     * @param body String
     * @return RawHttpResult
     */
    public RawHttpResult call(String domain, String path, HttpMethod httpMethod, String body) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        RawHttpResult result = new RawHttpResult();
        if (!path.isEmpty() && !path.startsWith("/"))
            path = "/" + path;
        String url = domain + path;
//        log.debug("API call URL: {}, method: {}, body: {}", url, httpMethod.name(), body);
        log.debug("API call URL: {}, method: {}:", url, httpMethod.name());
        try {
            switch (httpMethod.name().toUpperCase()) {
                case "GET":
                    doGet(url, result);
                    break;
                case "POST":
                    doPost(url, body, result);
                    break;
                case "PUT":
                    doPut(url, body, result);
                    break;
                case "DELETE":
                    doDelete(url, result);
                    break;
            }
        } catch (Exception e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
        return result;
    }

    private void doGet(String url, RawHttpResult result) {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        this.processResult(url, response, result);
    }

    private void doPost(String url, String body, RawHttpResult result) {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<String>(body, headers), String.class);
        this.processResult(url, response, result);
    }

    private void doPut(String url, String body, RawHttpResult result) {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<String>(body, headers), String.class);
        this.processResult(url, response, result);
    }

    private void doDelete(String url, RawHttpResult result) {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        this.processResult(url, response, result);
    }

    private void processResult(String url, ResponseEntity<String> response, RawHttpResult result) {
        result.setHttpStatus("" + response.getStatusCodeValue());
        result.setBody(response.getBody());
        log.info("CompreFace API result HTTP: {}, body: {}", result.getHttpStatus(), result.getBody());
        if (response.getStatusCodeValue() > 201) {
            Error error = new Error();
            error.setField(url);
            error.setMessage("");
            error.setStatus(response.getStatusCodeValue());
            error.setError("CompreFace API Error");
            result.setError(error);
            result.setSuccess(false);
        } else {
            result.setSuccess(true);
        }
    }

}
