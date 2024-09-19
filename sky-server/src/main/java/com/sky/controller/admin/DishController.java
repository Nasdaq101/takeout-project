package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("admin/dish")
@Api(tags = "dish interface")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * new dish
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("new dish")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("new dish:{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("search dish")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("dish query:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("delete dish")
    public Result delete(@RequestParam List<Long> ids){
        log.info("delete dish：{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("search dish using id")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("search dish by id：{}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("update dish info")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("update dish info；{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * start or stop dish
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        // 1. start 0, stop
        log.info("start or stop dish：{}", id);
        dishService.startOrStop(status, id);
        clearRedis("dish_*");
        return Result.success();
    }

    /**
     * search dish by category id
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> dishList = dishService.list(categoryId);
        return Result.success(dishList);
    }

    private void clearRedis(String keys) {
        Set<String> cacheKeys = redisTemplate.keys(keys);
        redisTemplate.delete(cacheKeys);
    }
}
