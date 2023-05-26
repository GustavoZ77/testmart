package com.testmart.demo.service;

import com.testmart.demo.model.*;
import com.testmart.demo.service.apireponse.ApiCartReponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService<Cart>{

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    String CARTS_URL = "https://dummyjson.com/carts/";
    private HttpHeaders headers;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProductService productService;

    Comparator<Cart> cartTotalComparator
            = Comparator.comparing(Cart::getTotal);

    public CartServiceImpl(){
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Override
    public List<Cart> getAllCarts() {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<ApiCartReponse> response = restTemplate.exchange(
                CARTS_URL,
                HttpMethod.GET,
                null,
                ApiCartReponse.class);
        Cart[] cartResponse = response.getBody().getCarts();
        return Arrays.asList(cartResponse);
    }

    @Override
    public Cart getCart(Integer cartId) {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<Cart> response = restTemplate.exchange(
                CARTS_URL+cartId,
                HttpMethod.GET,
                null,
                Cart.class);
        return response.getBody();
    }

    @Override
    public List<Cart> getUserCarts(Integer userId) {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<ApiCartReponse> response = restTemplate.exchange(
                CARTS_URL+"user/"+userId,
                HttpMethod.GET,
                null,
                ApiCartReponse.class);

        Cart[] cartResponse = response.getBody().getCarts();
        return Arrays.asList(cartResponse);
    }

    public Cart getCartWithHighestTotal(){
        List<Cart> carts = this.getAllCarts();
        Cart maxCart = carts.stream().max(cartTotalComparator).get();
        return maxCart;
    }

    public Cart getCartWithLowestTotal(){
        List<Cart> carts = this.getAllCarts();
        Cart maxCart = carts.stream().min(cartTotalComparator).get();
        return maxCart;
    }

    public List<Product> addProductImagesToUserCart(Integer userId){
        List<Product> listOfProducts = null;
        List <Cart>carts = this.getUserCarts(userId);
        Cart cartResponse = carts.size() > 0 ? carts.get(0): null;
        if(cartResponse != null){
            Product[] products = cartResponse.getProducts();
            // setting images from Products API
            listOfProducts = Arrays.stream(products).map(p -> {
                Product nP = (Product) productService.getProduct(p.getId());
                p.setImages(nP.getImages());
                return p;
            }).collect(Collectors.toCollection(ArrayList::new));
        }else{
            logger.warn("empty list of products");
        }
        return listOfProducts;
    }
}
