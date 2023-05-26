package com.testmart.demo.service.apireponse;

import com.testmart.demo.model.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiCategoryResponse {

    private Category[] categories;

}
