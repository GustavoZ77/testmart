package com.testmart.demo.service;

import com.testmart.demo.model.Product;
import com.testmart.demo.service.apireponse.ApiProductResponse;
import com.testmart.demo.utils.ResponseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ApiProductResponse apiProductResponse;

    @Mock
    private ResponseFactory<ApiProductResponse> apiProductResponseResponseFactory = new ResponseFactory<>();

    @Mock
    private ResponseFactory<Product> productResponseFactory = new ResponseFactory<>();

    @InjectMocks
    private ProductService productService = new ProductServiceImpl();
    final String PRODUCTS_URL = "https://dummyjson.com/products";

    final String PRODUCTS_URL_PARAM = "https://dummyjson.com/products?limit=1&skip=2&select=title,price,rating";

    private Product productMock;

    @BeforeEach
    public void setUp(){
        productMock = Product.builder().id(1).title("P1").build();
    }

    public void setUpProductApiResponse(Product[] products, String url){
        Mockito.when(apiProductResponse.getProducts()).thenReturn(products);
        Mockito.when(apiProductResponseResponseFactory.execute(url, ApiProductResponse.class)).thenReturn(apiProductResponse);
    }

    @Test
    public void getAllProductTest(){
        Product[] products = new Product[1];
        products[0] = productMock;
        setUpProductApiResponse(products, PRODUCTS_URL);
        List<Product> productsResponse = productService.getAllProducts();
        assertTrue(productsResponse.size() > 0);
    }

    @Test
    public void getAllProductParameterTest(){
        Product[] products = new Product[1];
        products[0] = Product.builder().id(1).title("P1").price(5.2f).rating(5f).images(new String[]{"IMG1","IMG2"}).build();
        Mockito.when(apiProductResponse.getProducts()).thenReturn(products);
        Mockito.when(apiProductResponseResponseFactory.execute(PRODUCTS_URL_PARAM, ApiProductResponse.class)).thenReturn(apiProductResponse);
        setUpProductApiResponse(products, PRODUCTS_URL_PARAM);
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
        Mockito.when(productResponseFactory.execute(PRODUCTS_URL+"/1", Product.class)).thenReturn(productMock);
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
        setUpProductApiResponse(products, PRODUCTS_URL+"/search?q="+query);
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
        setUpProductApiResponse(products, PRODUCTS_URL);
        List<String> res = productService.getProductTitlesByWorseRating(raiting);
        assertTrue(expectedResults.equals(res));
    }

}
