package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/dish")
@Api(tags = "dish interface")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

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
}
