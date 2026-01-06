package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.banxico.DataSerieResponse;
import com.travelandrepeat.api.dto.banxico.Response;
import com.travelandrepeat.api.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
@Service
@Slf4j
public class BanxicoServiceImpl implements BanxicoService {

    private static final int FIRST_SERIES_INDEX = 0;

    @Value("${banxico.api-url}")
    private String apiUrl;

    @Value("${banxico.token}")
    private String token;

    @Override
    public String getExchangeRate() {
        String result = "--.--";
        log.info("Fetching exchange rate from Banxico API");
        try {
            Response response = fetchExchangeRateResponse();
            double exchangeRate = extractExchangeRate(response);
            result = AppUtils.doubleToString(exchangeRate, 2);
            log.info("Successfully retrieved exchange rate: {}", result);
        } catch (Exception e) {
            log.error("Error with Banxico API", e);
        }
        return result;
    }

    private Response fetchExchangeRateResponse() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return restTemplate.getForObject(apiUrl + token, Response.class, headers);
    }

    private double extractExchangeRate(Response response) {
        List<DataSerieResponse> datos = response.bmx().series().get(FIRST_SERIES_INDEX).datos();
        return datos.get(FIRST_SERIES_INDEX).dato();
    }

}
