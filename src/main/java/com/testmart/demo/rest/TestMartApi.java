package com.testmart.demo.rest;

import com.testmart.demo.model.Cart;
import com.testmart.demo.model.Product;
import com.testmart.demo.service.CartService;
import com.testmart.demo.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/testmart/")
public class TestMartApi {

    private static final Logger logger = LoggerFactory.getLogger(TestMartApi.class);

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @GetMapping("/products/lowrating/{rating}")
    public ResponseEntity<List<Product>> getProductTitlesByWorseRating(@PathVariable double rating) {
        List<Product> products = productService.getProductTitlesByWorseRating(rating);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/cart/highest")
    public ResponseEntity<Cart> getCartWithHighestTotal() {
        Cart cart = (Cart) cartService.getCartWithHighestTotal();
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/cart/lowest")
    public ResponseEntity<Cart> getCartWithLowestTotal() {
        Cart cart = (Cart) cartService.getCartWithLowestTotal();
        return ResponseEntity.ok(cart);

    }

    @GetMapping("/cart/{userId}")
    public ResponseEntity<List<Product>> addProductImagesToUserCart(@PathVariable Integer userId) {
        List products = cartService.addProductImagesToUserCart(userId);
        if(products != null){
            return ResponseEntity.ok(products);
        }else {
            logger.warn("empty list of products");
            return ResponseEntity.notFound().build();
        }
    }
}
