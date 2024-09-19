package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * new setmeal
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * search setmeal
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * delete setmeal
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * search setmeal by id
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * update setmeal
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * start/ban setmeal
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * search list
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * search dish by id
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}