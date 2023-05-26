package com.testmart.demo.service.apireponse;

import com.testmart.demo.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiProductResponse {

    private Product[] products;

}
