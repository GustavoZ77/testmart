package com.testmart.demo.service;

import com.testmart.demo.model.Product;
import com.testmart.demo.model.User;
import com.testmart.demo.service.apireponse.ApiUserReponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApiUserReponse apiUserReponse;

    private final String USERS_URL = "https://dummyjson.com/users";

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Test
    public void getAllUsersTest(){

        User[] users = new User[2];
        users[0] = User.builder().id(1).firstName("User1").email("user1@mail.com").build();
        users[1] = User.builder().id(1).firstName("User2").email("user2@mail.com").build();

        Mockito.when(apiUserReponse.getUsers()).thenReturn(users);
        Mockito.when(restTemplate.exchange(
                        USERS_URL,
                        HttpMethod.GET,
                        null,
                        ApiUserReponse.class))
                .thenReturn(new ResponseEntity(apiUserReponse, HttpStatus.OK));
        List<Product> usersResponse = userService.getAllUsers();
        assertTrue(usersResponse.size() > 0);

    }

    @Test
    public void getUser(){

        User user = User.builder().id(1).firstName("User1").email("user1@mail.com").build();
        Integer userId = 1;
        Mockito.when(restTemplate.exchange(
                        USERS_URL+"/"+userId,
                        HttpMethod.GET,
                        null,
                        User.class))
                .thenReturn(new ResponseEntity(user, HttpStatus.OK));
        User usersResponse = (User) userService.getUser(userId);
        assertTrue(usersResponse.getId().equals(1));

    }
}
