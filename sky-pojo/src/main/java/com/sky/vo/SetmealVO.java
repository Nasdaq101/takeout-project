package com.sky.vo;

import com.sky.entity.SetmealDish;
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
public class SetmealVO implements Serializable {

    private Long id;

    //categury id
    private Long categoryId;

    //setmeal name
    private String name;

    //setmeal price
    private BigDecimal price;

    //start 1 stop 0
    private Integer status;

    //description info
    private String description;

    //image
    private String image;

    //update time
    private LocalDateTime updateTime;

    //category name
    private String categoryName;

    //setmeal and dishes
    @Builder.Default
    private List<SetmealDish> setmealDishes = new ArrayList<>();
}
