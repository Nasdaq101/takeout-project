package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "shop interface")
@Slf4j
public class ShopController {

    public static final String KEY="SHOP_STATUS";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * set shop status
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("set shop status")
    public Result setStatus(@PathVariable Integer status) {
        log.info("set shop status as: {}",status == 1 ? "shop closed" : "shop open");
        redisTemplate.opsForValue().set("SHOP_STATUS", status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("get shop status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("get shop status as: {}", status == 1 ? "shop closed" : "shop open");
        return Result.success(status);
    }
}
