package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * order submit
     *
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * order payment
     *
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * successful payment, update order status
     *
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * get order history
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQueryByUser(int page, int pageSize, Integer status);

    /**
     * get order details
     *
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * cancel order
     *
     * @param id
     */
    void userCancelById(Long id) throws Exception;

    /**
     * order again
     *
     * @param id
     */
    void repetition(Long id);

    /**
     * order - condition search
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * amount of the order for various status
     *
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * confirm order
     *
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * reject order
     *
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * cancel order
     *
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * delivery
     *
     * @param id
     */
    void delivery(Long id);

    /**
     * complete order
     *
     * @param id
     */
    void complete(Long id);

    /**
     * order reminder
     *
     * @param id
     */
    void reminder(Long id);

}
