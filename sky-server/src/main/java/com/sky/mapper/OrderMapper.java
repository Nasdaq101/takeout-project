package com.sky.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;

@Mapper
public interface OrderMapper {

    /**
     * insert data to order table
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * get order details by number
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * update order details
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * page query
     *
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * get order by id
     *
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * get order amount based on status
     *
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * get order amount based on status and order time(less than xx)
     *
     * @param status
     * @param orderTimeLT
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTimeLT}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTimeLT);

    /**
     * sum turnover by map
     *
     * @param map
     */
    Double sumByMap(Map<String, Object> map);

    /**
     * count order amount by map
     *
     * @param map
     */
    Integer countByMap(Map<String, Object> map);

    /**
     * get ranking of top sales
     *
     * @param begin
     * @param end
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}