package com.testmart.demo.service;

import com.testmart.demo.model.*;
import com.testmart.demo.service.apireponse.ApiCartReponse;
import com.testmart.demo.utils.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService<Cart>{

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    String CARTS_URL = "https://dummyjson.com/carts/";

    @Autowired
    ProductService productService;

    @Autowired
    ResponseFactory<Cart> cartResponseFactory;

    @Autowired
    ResponseFactory<ApiCartReponse> apiCartReponseResponseFactory;

    Comparator<Cart> cartTotalComparator
            = Comparator.comparing(Cart::getTotal);

    @Override
    public List<Cart> getAllCarts() {
        Cart[] cartResponse = apiCartReponseResponseFactory.execute(CARTS_URL, ApiCartReponse.class).getCarts();
        return Arrays.asList(cartResponse);
    }

    @Override
    public Cart getCart(Integer cartId) {
        return cartResponseFactory.execute(CARTS_URL+cartId, Cart.class);
    }

    @Override
    public List<Cart> getUserCarts(Integer userId) {
        Cart[] cartResponse = apiCartReponseResponseFactory.execute(CARTS_URL+"user/"+userId, ApiCartReponse.class).getCarts();
        return List.of(cartResponse);
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
