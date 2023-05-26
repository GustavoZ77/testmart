package com.testmart.demo.service;

import java.util.List;

// Note: the generic type parameters P and C are used to represent the types of the product and category, respectively
public interface ProductService<P, C> {

    /*
     * Get all products of TestMart
     * API endpoint to get data: https://dummyjson.com/products
     */
    List<P> getAllProducts();

    /*
     * Get all products of TestMart using parameters
     * API endpoint to get data: https://dummyjson.com/products?limit={limit}&skip={skip}&select={comma separated
     * fields of product}
     */
    List<P> getAllProducts(int limit, int skip, String... fields);

    /*
     * Get a single product
     * API endpoint to get data: https://dummyjson.com/products/{productId}
     */
    P getProduct(Integer productId);

    /**
     * Search for products in TestMart
     * API endpoint to get data: https://dummyjson.com/products/search?q={query}
     */
    List<P> searchProducts(String query);

    /*
     * Get all products categories in TestMart
     * API endpoint to get data: https://dummyjson.com/products/categories
     */
    List<C> getCategories();

    /*
     * Get all products of a category
     * API endpoint to get data: https://dummyjson.com/products/category/{categoryName}
     */
    List<P> getProductsByCategory(String categoryName);

    List<String> getProductTitlesByWorseRating(double raiting);
}
