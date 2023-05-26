package com.testmart.demo.service;

import com.testmart.demo.service.apireponse.ApiCategoryResponse;
import com.testmart.demo.service.apireponse.ApiProductResponse;
import com.testmart.demo.model.Category;
import com.testmart.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService<Product, Category> {

    private final String PRODUCTS_URL = "https://dummyjson.com/products";

    private final String PRODUCTS_BY_CATEGORIES_URL = "https://dummyjson.com/products/category/";

    private HttpHeaders headers;

    @Autowired
    private RestTemplate restTemplate;


    public ProductServiceImpl(){
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Override
    public List<Product> getAllProducts() {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<ApiProductResponse> response = restTemplate.exchange(
                PRODUCTS_URL,
                HttpMethod.GET,
                null,
                ApiProductResponse.class);
        Product[] productsResponse = response.getBody().getProducts();
        return Arrays.asList(productsResponse);
    }

    @Override
    public List<Product> getAllProducts(int limit, int skip, String... selectValues) {
        ResponseEntity<ApiProductResponse> response = restTemplate.exchange(
                PRODUCTS_URL+"?limit="+limit+"&skip="+skip+"&select="+String.join(",", Arrays.asList(selectValues)),
                HttpMethod.GET,
                null,
                ApiProductResponse.class);
        Product[] productsResponse = response.getBody().getProducts();
        return Arrays.asList(productsResponse);
    }

    @Override
    public Product getProduct(Integer productId) {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<Product> response = restTemplate.exchange(
                PRODUCTS_URL+"/"+productId,
                HttpMethod.GET,
                null,
                Product.class);
        return response.getBody();
    }

    @Override
    public List<Product> searchProducts(String query) {
        ResponseEntity<ApiProductResponse> response = restTemplate.exchange(
                PRODUCTS_URL+"/search?q="+query,
                HttpMethod.GET,
                null,
                ApiProductResponse.class);
        Product[] productsResponse = response.getBody().getProducts();
        return Arrays.asList(productsResponse);
    }

    @Override
    public List<Category> getCategories() {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<ApiCategoryResponse> response = restTemplate.exchange(
                PRODUCTS_URL,
                HttpMethod.GET,
                null,
                ApiCategoryResponse.class);
        Category[] categoryResponse = response.getBody().getCategories();
        return Arrays.asList(categoryResponse);
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<ApiProductResponse> response = restTemplate.exchange(
                PRODUCTS_BY_CATEGORIES_URL+categoryName,
                HttpMethod.GET,
                null,
                ApiProductResponse.class);
        Product[] categoryResponse = response.getBody().getProducts();
        return Arrays.asList(categoryResponse);
    }

    public List<String> getProductTitlesByWorseRating(double raiting){
        List<Product> products = this.getAllProducts();
        // predicate to filter
        BiPredicate<Product, Double> worseRatingFilter = (product, rate) -> product.getRating() <= rate;
        List<String> productTitles;
        // filtering the products list
        productTitles = products.stream().filter(p -> worseRatingFilter.test(p, raiting)).map(Product::getTitle).collect(Collectors.toCollection(ArrayList::new));
        return productTitles;
    }
}
