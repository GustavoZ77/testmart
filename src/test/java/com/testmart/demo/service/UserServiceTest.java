package com.testmart.demo.service;

import com.testmart.demo.model.Product;
import com.testmart.demo.model.User;
import com.testmart.demo.service.apireponse.ApiUserReponse;
import com.testmart.demo.utils.ResponseFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private ApiUserReponse apiUserReponse;

    @Mock
    private ResponseFactory<ApiUserReponse> apiUserResponseResponseFactory = new ResponseFactory<>();

    @Mock
    private ResponseFactory<User> userResponseFactory = new ResponseFactory<>();

    private final String USERS_URL = "https://dummyjson.com/users";

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Test
    public void getAllUsersTest(){
        User[] users = new User[2];
        users[0] = User.builder().id(1).firstName("User1").email("user1@mail.com").build();
        users[1] = User.builder().id(1).firstName("User2").email("user2@mail.com").build();
        Mockito.when(apiUserReponse.getUsers()).thenReturn(users);
        Mockito.when(apiUserResponseResponseFactory.execute(USERS_URL, ApiUserReponse.class)).thenReturn(apiUserReponse);
        List<Product> usersResponse = userService.getAllUsers();
        assertTrue(usersResponse.size() > 0);
    }

    @Test
    public void getUser(){
        User user = User.builder().id(1).firstName("User1").email("user1@mail.com").build();
        Integer userId = 1;
        Mockito.when(userResponseFactory.execute(USERS_URL+"/"+userId, User.class)).thenReturn(user);
        User usersResponse = (User) userService.getUser(userId);
        assertTrue(usersResponse.getId().equals(1));
    }
}
