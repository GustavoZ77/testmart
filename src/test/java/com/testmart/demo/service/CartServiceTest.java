package com.testmart.demo.service;

import com.testmart.demo.model.Cart;
import com.testmart.demo.model.Product;
import com.testmart.demo.service.apireponse.ApiCartReponse;
import com.testmart.demo.utils.ResponseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private ApiCartReponse apiCartReponse;

    @Mock
    private ResponseFactory<ApiCartReponse> apiCartResponseResponseFactory = new ResponseFactory<>();

    @Mock
    private ResponseFactory<Cart> cartResponseFactory = new ResponseFactory<>();

    @Mock
    private ProductService productService = new ProductServiceImpl();

    @InjectMocks
    private CartService cartService = new CartServiceImpl();

    final String CARTS_URL = "https://dummyjson.com/carts/";
    final Integer USER_ID = 5;
    final Integer CART_ID = 1;
    Cart cartMock;

    @BeforeEach
    public void setUp(){
        cartMock = Cart.builder().id(CART_ID).total(5f).userId(5).total(5f).build();
    }

    private void setUpCartApiResponse(Cart[] carts, String url){
        Mockito.when(apiCartReponse.getCarts()).thenReturn(carts);
        Mockito.when(apiCartResponseResponseFactory.execute(url, ApiCartReponse.class)).thenReturn(apiCartReponse);
    }

    @Test
    public void getAllCartsTest(){
        Cart[] carts = new Cart[2];
        carts[0] = cartMock;
        setUpCartApiResponse(carts, CARTS_URL);
        List cartsResponse = cartService.getAllCarts();
        assertTrue(cartsResponse.size() > 0);
    }

    @Test
    public void getAllCartsNullTest(){
        Cart[] carts = new Cart[2];
        setUpCartApiResponse(carts, CARTS_URL);
        List cartsResponse = cartService.getAllCarts();
        assertTrue(cartsResponse.size() > 0);
    }

    @Test
    public void getCartTest(){
        Mockito.when(cartResponseFactory.execute(CARTS_URL+"1", Cart.class)).thenReturn(cartMock);
        Cart cart = (Cart) cartService.getCart(1);
        assertTrue(cart.getId().equals(cartMock.getId()));
    }

    @Test
    public void getUserCartsTest(){
        Cart[] carts = new Cart[2];
        carts[0] = cartMock;
        carts[1] = cartMock;
        carts[1].setTotal(1f);
        carts[1].setId(2);
        setUpCartApiResponse(carts, CARTS_URL+"user/"+USER_ID);
        List<Cart> cartsResponse = cartService.getUserCarts(USER_ID);
        cartsResponse.forEach(p -> p.getUserId().equals(USER_ID));
    }

    @Test
    public void getCartWithHighestTotalTest(){
        Cart[] carts = new Cart[2];
        carts[0] = Cart.builder().id(1).total(5f).userId(5).build();
        carts[1] = Cart.builder().id(2).total(1f).userId(5).build();
        setUpCartApiResponse(carts, CARTS_URL);
        Cart cart = (Cart) cartService.getCartWithHighestTotal();
        assertTrue(cart.getId().equals(1));
    }

    @Test
    public void getCartWithLowesTotalTest(){
        Cart[] carts = new Cart[2];
        carts[0] = Cart.builder().id(1).total(5f).userId(5).build();
        carts[1] = Cart.builder().id(2).total(1f).userId(5).build();
        setUpCartApiResponse(carts, CARTS_URL);
        Cart cart = (Cart) cartService.getCartWithLowestTotal();
        assertTrue(cart.getId().equals(2));
    }

    @Test
    public void addProductImagesToUserCartTest(){

        Product p1 = Product.builder().id(1).title("P1").images(new String[]{"IMG1","IMG2"}).build();
        Product p2 = Product.builder().id(2).title("P2").images(new String[]{"IMG1","IMG2"}).build();

        Cart[] carts = new Cart[2];
        carts[0] = Cart.builder().id(1).total(5f).userId(5).products(new Product[]{p1, p2}).build();
        carts[1] = Cart.builder().id(2).total(1f).userId(5).products(new Product[]{p1, p2}).build();
        setUpCartApiResponse(carts, CARTS_URL+"user/"+USER_ID);
        Mockito.when(productService.getProduct(1)).thenReturn( new ResponseEntity( p1, HttpStatus.OK).getBody());
        Mockito.when(productService.getProduct(2)).thenReturn( new ResponseEntity( p2, HttpStatus.OK).getBody());
        List<Product> productsReponse = cartService.addProductImagesToUserCart(USER_ID);
        productsReponse.forEach(p -> {
            assertNotNull(p.getImages());
            assertTrue(p.getImages().length > 0);
        });
    }

}
