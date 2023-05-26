package com.testmart.demo.service;

import com.testmart.demo.model.User;
import com.testmart.demo.service.apireponse.ApiUserReponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService<User>{


    String USERS_URL = "https://dummyjson.com/users";

    private HttpHeaders headers;

    @Autowired
    RestTemplate restTemplate;

    public UserServiceImpl(){
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Override
    public List<User> getAllUsers() {

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<ApiUserReponse> response = restTemplate.exchange(
                USERS_URL,
                HttpMethod.GET,
                null,
                ApiUserReponse.class);
        User[] usersResponse = response.getBody().getUsers();
        return Arrays.asList(usersResponse);

    }

    @Override
    public User getUser(Integer userId) {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<User> response = restTemplate.exchange(
                USERS_URL+"/"+userId,
                HttpMethod.GET,
                null,
                User.class);
        return response.getBody();
    }

    @Override
    public List<User> searchUsers(String query) {
        ResponseEntity<ApiUserReponse> response = restTemplate.exchange(
                USERS_URL+"/search?q"+query,
                HttpMethod.GET,
                null,
                ApiUserReponse.class);
        User[] productsResponse = response.getBody().getUsers();
        return Arrays.asList(productsResponse);
    }

}
