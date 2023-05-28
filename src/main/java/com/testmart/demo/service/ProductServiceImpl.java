package com.testmart.demo.service;

import com.testmart.demo.service.apireponse.ApiCategoryResponse;
import com.testmart.demo.service.apireponse.ApiProductResponse;
import com.testmart.demo.model.Category;
import com.testmart.demo.model.Product;
import com.testmart.demo.utils.ResponseFactory;
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

    @Autowired
    private ResponseFactory<Product> productResponseFactory;

    @Autowired
    private ResponseFactory<ApiCategoryResponse> categoryResponseFactory;

    @Autowired
    private ResponseFactory<ApiProductResponse> apiProductResponseResponseFactory;

    @Override
    public List<Product> getAllProducts() {
        return List.of(apiProductResponseResponseFactory.execute(PRODUCTS_URL, ApiProductResponse.class).getProducts());
    }

    @Override
    public List<Product> getAllProducts(int limit, int skip, String... selectValues) {
        String sUrl = PRODUCTS_URL+"?limit="+limit+"&skip="+skip+"&select="+String.join(",", Arrays.asList(selectValues));
        return List.of(apiProductResponseResponseFactory.execute(sUrl, ApiProductResponse.class).getProducts());
    }

    @Override
    public Product getProduct(Integer productId) {
        return productResponseFactory.execute( PRODUCTS_URL+"/"+productId, Product.class);
    }

    @Override
    public List<Product> searchProducts(String query) {
        String sUrl = PRODUCTS_URL+"/search?q="+query;
        return List.of(apiProductResponseResponseFactory.execute(sUrl, ApiProductResponse.class).getProducts());
    }

    @Override
    public List<Category> getCategories() {
      return List.of(categoryResponseFactory.execute(PRODUCTS_BY_CATEGORIES_URL, ApiCategoryResponse.class).getCategories());
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        String sUrl = PRODUCTS_BY_CATEGORIES_URL+categoryName;
        return List.of(apiProductResponseResponseFactory.execute(sUrl, ApiProductResponse.class).getProducts());
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
