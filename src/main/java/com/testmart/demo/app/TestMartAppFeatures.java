package com.testmart.demo.app;

import com.testmart.demo.model.Cart;
import com.testmart.demo.model.Product;
import com.testmart.demo.service.CartService;
import com.testmart.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// Note: Convert this class to concrete class and add implementation (missing body) to all methods. You will remove the word
// `abstract` everywhere. This class is only kept `abstract` for the sake of interview exercise.
public class TestMartAppFeatures {

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    /**
     * Prints the titles of all products that have a rating less than or equal to the provided criteria.
     *
     * @param rating The rating threshold.
     */
    public void getProductTitlesByWorseRating(double rating){
        List<Product> products = productService.getProductTitlesByWorseRating(rating);
        products.forEach(System.out::println);
    }

    /**
     * Returns the cart with the highest total value.
     * @returns The cart with the highest total value.
     */
    public Cart getCartWithHighestTotal(){
        return (Cart) cartService.getCartWithHighestTotal();
    }

    /**
     * Returns the cart with the lowest total value.
     * @returns The cart with the lowest total value.
     */
    public Cart getCartWithLowestTotal(){
        return (Cart) cartService.getCartWithLowestTotal();
    }

    /**
     * Enriches the product information in a user's cart by adding product images.
     * The current product information in a cart has limited fields.
     * This method adds the `images` field for each product in a given user's cart.
     * Note: This method only applies to the first element from the `carts[]` JSON response.
     * @param userId The ID of the user whose cart's product information will be enriched.
     * @returns A list of products with enriched information in the user's cart.
     */
    public List<Product> addProductImagesToUserCart(Integer userId){
        return cartService.addProductImagesToUserCart(userId);
    }
}