package com.testmart.demo.service;

import com.testmart.demo.model.Product;
import com.testmart.demo.service.apireponse.ApiProductResponse;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApiProductResponse apiProductResponse;

    @Mock
    private ResponseEntity responseEntity;

    @InjectMocks
    private CartService cartService = new CartServiceImpl();

    @InjectMocks
    private ProductService productService = new ProductServiceImpl();
    final String PRODUCTS_URL = "https://dummyjson.com/products";
    final String PRODUCTS_URL_PARAM = "https://dummyjson.com/products?limit=1&skip=2&select=title,price,rating";

    final Integer USER_ID = 5;
    final Integer CART_ID = 1;

    private Product productMock;

    @BeforeEach
    public void setUp(){
        productMock = Product.builder().id(1).title("P1").build();
    }

    @Test
    public void getAllProductTest(){
        Product[] products = new Product[2];
        products[0] = productMock;
        Mockito.when(apiProductResponse.getProducts()).thenReturn(products);
        Mockito.when(restTemplate.exchange(
                        PRODUCTS_URL,
                        HttpMethod.GET,
                        null,
                        ApiProductResponse.class))
                .thenReturn(new ResponseEntity(apiProductResponse, HttpStatus.OK));
        List<Product> productsResponse = productService.getAllProducts();
        assertTrue(productsResponse.size() > 0);
    }

    @Test
    public void getAllProductParameterTest(){
        Product[] products = new Product[1];

        products[0] = Product.builder().id(1).title("P1").price(5.2f).rating(5f).images(new String[]{"IMG1","IMG2"}).build();

        Mockito.when(apiProductResponse.getProducts()).thenReturn(products);
        Mockito.when(restTemplate.exchange(
                        PRODUCTS_URL_PARAM,
                        HttpMethod.GET,
                        null,
                        ApiProductResponse.class))
                .thenReturn(new ResponseEntity(apiProductResponse, HttpStatus.OK));
        List<Product> productsResponse = productService.getAllProducts(1,2, new String[]{"title", "price", "rating"});
        assertTrue(productsResponse.size() == 1);
        productsResponse.forEach(p -> {
            assertNotNull(p.getTitle());
            assertNotNull(p.getPrice());
            assertNotNull(p.getRating());
            assertTrue(p.getId() != 2);
        });
    }

    @Test
    public void getProductTest(){
        Mockito.when(restTemplate.exchange(
                        PRODUCTS_URL+"/1",
                        HttpMethod.GET,
                        null,
                        Product.class))
                .thenReturn(new ResponseEntity(productMock, HttpStatus.OK));
        Product product = (Product) productService.getProduct(1);
        assertTrue(product.getId().equals(productMock.getId()));
    }

    @Test
    public void searchProductsTest(){
        Product[] products = new Product[4];

        products[0] = Product.builder().id(1).title("phone").price(5.2f).rating(5f).images(new String[]{"IMG1","IMG2"}).build();
        products[1] = Product.builder().id(2).title("phone").price(5.2f).rating(5f).images(new String[]{"IMG1","IMG2"}).build();
        products[2] = Product.builder().id(3).title("phone").price(5.2f).rating(5f).images(new String[]{"IMG1","IMG2"}).build();
        products[3] = Product.builder().id(4).title("phone").price(5.2f).rating(5f).images(new String[]{"IMG1","IMG2"}).build();

        String query = "phone";
        Mockito.when(apiProductResponse.getProducts()).thenReturn(products);
        Mockito.when(restTemplate.exchange(
                        PRODUCTS_URL+"/search?q="+query,
                        HttpMethod.GET,
                        null,
                        ApiProductResponse.class))
                .thenReturn(new ResponseEntity(apiProductResponse, HttpStatus.OK));
        List<Product> productsResponse = productService.searchProducts(query);
        assertTrue(productsResponse.size() > 0);
        productsResponse.forEach(p -> {
            assertTrue(p.getTitle().equals(query));
        });
    }

    @Test
    public void getProductTitlesByWorseRating(){
        Product[] products = new Product[4];
        double raiting = 4;

        List<String> expectedResults = List.of(new String[]{"p2", "p3", "p4"});

        products[0] = Product.builder().id(1).title("p1").price(5.2f).rating(5f).images(new String[]{"IMG1","IMG2"}).build();
        products[1] = Product.builder().id(2).title("p2").price(5.2f).rating(4f).images(new String[]{"IMG1","IMG2"}).build();
        products[2] = Product.builder().id(3).title("p3").price(5.2f).rating(3f).images(new String[]{"IMG1","IMG2"}).build();
        products[3] = Product.builder().id(4).title("p4").price(5.2f).rating(1f).images(new String[]{"IMG1","IMG2"}).build();

        Mockito.when(apiProductResponse.getProducts()).thenReturn(products);
        Mockito.when(restTemplate.exchange(
                        PRODUCTS_URL,
                        HttpMethod.GET,
                        null,
                        ApiProductResponse.class))
                .thenReturn(new ResponseEntity(apiProductResponse, HttpStatus.OK));

        List<String> res = productService.getProductTitlesByWorseRating(raiting);
        assertTrue(expectedResults.equals(res));
    }

}
