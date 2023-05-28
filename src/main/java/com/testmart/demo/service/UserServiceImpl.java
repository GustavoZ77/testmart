package com.testmart.demo.service;

import com.testmart.demo.model.Cart;
import com.testmart.demo.model.User;
import com.testmart.demo.service.apireponse.ApiCartReponse;
import com.testmart.demo.service.apireponse.ApiCategoryResponse;
import com.testmart.demo.service.apireponse.ApiProductResponse;
import com.testmart.demo.service.apireponse.ApiUserReponse;
import com.testmart.demo.utils.ResponseFactory;
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

    @Autowired
    ResponseFactory<User> userResponseFactory;

    @Autowired
    ResponseFactory<ApiUserReponse> apiUserReponseResponseFactory;
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
        User[] usersResponse = apiUserReponseResponseFactory.execute(USERS_URL, ApiUserReponse.class).getUsers();
        return Arrays.asList(usersResponse);
    }

    @Override
    public User getUser(Integer userId) {
        User usersResponse = userResponseFactory.execute(USERS_URL+"/"+userId, User.class);
        return usersResponse;
    }

    @Override
    public List<User> searchUsers(String query) {
        User[] usersResponse = apiUserReponseResponseFactory.execute(USERS_URL+"/search?q"+query, ApiUserReponse.class).getUsers();
        return Arrays.asList(usersResponse);
    }

}
