package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
    /**
     * new dish and its flavor data
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);
}
