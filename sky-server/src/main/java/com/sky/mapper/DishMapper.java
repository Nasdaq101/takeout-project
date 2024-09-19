package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * find the amount of dish based on dish ID.
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * insert dish data
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * dish query
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    /**
     * delete dish based on id
     * @param id
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);


    /**
     * update dish dynamically based on id
     * @param dish
     */
    @AutoFill(value=OperationType.UPDATE)
    void update(Dish dish);

    /**
     * search dish by category id
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * get dish by setmeal id
     * @param setmealId
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * count by map
     * @param map
     * @return
     */
    Integer countByMap(Map map);

}
