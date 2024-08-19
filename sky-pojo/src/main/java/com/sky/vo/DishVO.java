package com.sky.vo;

import com.sky.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishVO implements Serializable {

    //dish id
    private Long id;
    //dish name
    private String name;
    //category id
    private Long categoryId;
    //price
    private BigDecimal price;
    //dish image
    private String image;
    //dish description
    private String description;
    //current status - 0 ban , 1 show
    private Integer status;
    //update time
    private LocalDateTime updateTime;
    //category name
    private String categoryName;
    // dish flavors
    private List<DishFlavor> flavors = new ArrayList<>();

    //private Integer copies;
}
