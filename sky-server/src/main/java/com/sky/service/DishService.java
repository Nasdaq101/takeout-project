package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * new dish and its flavor data
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * dish query
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * delete dish (batch available)
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * search dish and its flavor by id
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * update dish info and its flavor info
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * start or stop dish
     *
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id);

    /**
     * search dish by category id
     *
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId);

    /**
     * search dish+flavor by category id
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
