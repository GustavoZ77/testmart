package com.testmart.demo.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product{

    private Integer id;
    private String title;
    private String description;
    private Float price;
    private Float rating;
    private String[] images;

    public String toString(){
        return title;
    }

}
