package maxz.monitor.services.callers;

import lombok.extern.slf4j.Slf4j;
import maxz.monitor.services.callers.exceptions.SetupException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseCaller {

    public String processRequest(String url) {
        return getRestTemplateNoExceptions().getForEntity(url, String.class).getBody();
    }

    private static RestTemplate getRestTemplateNoExceptions() {
//        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        httpRequestFactory.setConnectionRequestTimeout(1_000);
//        httpRequestFactory.setConnectTimeout(1_000);
//        httpRequestFactory.setReadTimeout(1_000);
//
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                System.out.println("response = " + response);
            }
        });

//        HttpComponentsClientHttpRequestFactory rf = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
//        rf.setReadTimeout(3_000);
//        rf.setConnectTimeout(3_000);

        return restTemplate;
    }



}
