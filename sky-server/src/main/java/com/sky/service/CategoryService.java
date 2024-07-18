package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import java.util.List;

public interface CategoryService {

    /**
     * new category
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * page query
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * remove category by id
     * @param id
     */
    void deleteById(Long id);

    /**
     * update category
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * start/stop category
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * category query by type
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
