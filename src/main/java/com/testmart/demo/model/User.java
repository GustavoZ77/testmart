package com.testmart.demo.model;

import lombok.*;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@Builder
public class User implements Serializable {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Cart> userCarts;

}
