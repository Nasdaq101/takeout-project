package com.sky.dto;

import com.sky.entity.DishFlavor;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDTO implements Serializable {

    // dish id
    private Long id;
    //dish name
    private String name;
    //category id
    private Long categoryId;
    //dish price
    private BigDecimal price;
    //dish pic
    private String image;
    //dish description
    private String description;
    //current status - 0 ban , 1 allow
    private Integer status;
    // dish flavors
    private List<DishFlavor> flavors = new ArrayList<>();

}
