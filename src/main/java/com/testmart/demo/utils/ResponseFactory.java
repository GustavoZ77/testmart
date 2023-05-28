package com.testmart.demo.utils;

import com.testmart.demo.model.Cart;
import com.testmart.demo.service.apireponse.ApiCartReponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class ResponseFactory<T> {

    private HttpHeaders headers;

    @Autowired
    RestTemplate restTemplate;

    public ResponseFactory(){
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    public T execute(String URL, Class<T> classOfReponse){
        ResponseEntity<T> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                null,
                classOfReponse);
        return (T) response.getBody();
    }
}
