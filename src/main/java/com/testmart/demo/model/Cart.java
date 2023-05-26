package com.testmart.demo.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart implements Serializable {
    private Integer id;
    private Float total;
    private Float discountedTotal;
    private Integer userId;
    private Integer totalProducts;
    private Integer totalQuantity;
    private Product[] products;
}
