package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Work Space
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "Workspace API Endpoints")
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * get business data
     *
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("Get Business data")
    public Result<BusinessDataVO> businessData() {
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.now().with(LocalTime.MIN), LocalDateTime.now().with(LocalTime.MAX));
        return Result.success(businessDataVO);
    }

    /**
     * get order overview
     *
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("Get Order Overview")
    public Result<OrderOverViewVO> orderOverView() {
        return Result.success(workspaceService.getOrderOverView());
    }

    /**
     * get dish overview
     *
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("Get Dish Overview")
    public Result<DishOverViewVO> dishOverView() {
        return Result.success(workspaceService.getDishOverView());
    }

    /**
     * get setmeal overview
     *
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("Get Setmeal Overview")
    public Result<SetmealOverViewVO> setmealOverView() {
        return Result.success(workspaceService.getSetmealOverView());
    }
}