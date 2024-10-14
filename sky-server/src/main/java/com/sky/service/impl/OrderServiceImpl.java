package com.sky.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.sky.websocket.WebSocketServer;
import org.apache.poi.ss.formula.functions.Odd;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
// import com.sky.entity.User;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import lombok.extern.slf4j.Slf4j;

import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
// import com.sky.mapper.UserMapper;
import com.sky.result.PageResult;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    // @Autowired
    // private UserMapper userMapper;

    /**
     * submit order
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // if addressBook/ shopping cart is empty?
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> cartList = shoppingCartMapper.list(shoppingCart);
        if (cartList == null || cartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // insert single data to order list
        Orders orders = new Orders(); //parameter type = Orders
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);

        orderMapper.insert(orders);

        // insert multiple data to order detail list.
        List<OrderDetail> orderDetailList = new ArrayList<>();
//        for (ShoppingCart cart: cartList)
        cartList.forEach(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        });
        orderDetailMapper.insertBatch(orderDetailList);

        // clean shopping-cart
        shoppingCartMapper.deleteByUserId(userId);

        // return vo
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();

        return orderSubmitVO;
    }

    /**
     * payment
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // current user id
        // Long userId = BaseContext.getCurrentId();
        // User user = userMapper.getById(userId);

        /**
         * directly call paySuccess method --- simulation of successful payment
         */
        paySuccess(ordersPaymentDTO.getOrderNumber());

        /**
         * call WECHAT payment api, generate a prepayment transaction order
         */
//         JSONObject jsonObject = weChatPayUtil.pay(
        // ordersPaymentDTO.getOrderNumber(), // order number
        // new BigDecimal(0.01), // amount + currency
        // "sky takeout order", // description
        // user.getOpenid() // user openid
        // );

        // if (jsonObject.getString("code") != null &&
        // jsonObject.getString("code").equals("ORDERPAID")) {
        // throw new OrderBusinessException("order has been paid");
        // }

        // OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        // vo.setPackageStr(jsonObject.getString("package"));

        // return vo;
        return null;
    }

    /**
     * payment success, update order details
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // get order by order number
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // update order status/payment status/time of payment by order id
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .packAmount(Orders.PAID).tablewareNumber(Orders.PAID).build();

        orderMapper.update(orders);

        // using websocket to inform merchant
        Map<String, Object> map = new HashMap<>();
        map.put("type", 1); // 1:order notification
        map.put("orderId", ordersDB.getId());
        map.put("content", "order number: " + outTradeNo);
        String msg = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(msg);

    }

    /**
     * get order history
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult pageQueryByUser(int page, int pageSize, Integer status) {
        // set pagination
        PageHelper.startPage(page, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // page query
        Page<Orders> pageOrders = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList<>();

        // get order details and encapsulate into orderVO
        if (pageOrders != null && pageOrders.getTotal() > 0) {
            for (Orders orders : pageOrders) {
                Long orderId = orders.getId();

                // get order details
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        long total = Optional.ofNullable(pageOrders).map(Page::getTotal).orElse(0L);
        return new PageResult(total, list);
    }

    /**
     * get order details
     *
     * @param id
     * @return
     */
    public OrderVO details(Long id) {
        // get order by id
        Orders orders = orderMapper.getById(id);

        // get dish/setmeal info
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // encapsulate this prder(and details) into OrderVO and return it.
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * cancel order
     *
     * @param id
     */
    public void userCancelById(Long id) throws Exception {
        // get order by id
        Orders ordersDB = orderMapper.getById(id);

        // check whether order exists
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // order status 1to_be_paid 2to_be_confirmed 3confirmed 4in_delivery 5completed 6cancelled
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // if order under status "to_be_confirmed", require refund
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            // call WECHAT pay/refund api
            weChatPayUtil.refund(
                    ordersDB.getNumber(), // order number
                    ordersDB.getNumber(), // order-refund number
                    new BigDecimal(0.01), // refund amount, currency
                    new BigDecimal(0.01));// original payment

            // payment status updated as REFUND
            orders.setPayStatus(Orders.REFUND);
        }

        // update order status(+reason/time of cancellation)
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("User cancelled");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * order again
     *
     * @param id
     */
    public void repetition(Long id) {
        // get current user id
        Long userId = BaseContext.getCurrentId();

        // get order details by user id
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        // transfer order detail object into shopping-cart object
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            // copy info into shopping-cart object
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        // shopping-cart object insert batch
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * search order
     *
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        // some order status, needs additional dish info. transfer DTO to VO
        List<OrderVO> orderVOList = getOrderVOList(page);

        return new PageResult(page.getTotal(), orderVOList);
    }

    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        // require return orderVOList， customize orderVO
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                // copy to OrderVO
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderDishes = getOrderDishesStr(orders);

                // encapsulate dish info into orderVO and add it to the orderVOList
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    /**
     * get dish string based on order id
     *
     * @param orders
     * @return
     */
    private String getOrderDishesStr(Orders orders) {
        // get detailed dish info of specific order (dish+amount)
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // join all dish info as string (format: dish_name*3)
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        // join information of all dish together in an order
        return String.join("", orderDishList);
    }

    /**
     * get amount of order in various status
     *
     * @return
     */
    public OrderStatisticsVO statistics() {
        // get order amount based on order status ( to_be_confirmed/confirmed/delivery_in_progress)
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);

        // encapsulate search results(data) as orderStatisticsVO
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
    }

    /**
     * receive(confirm) order
     *
     * @param ordersConfirmDTO
     */
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .packAmount(Orders.PAID).tablewareNumber(Orders.PAID).build();

        orderMapper.update(orders);
    }

    /**
     * reject order
     *
     * @param ordersRejectionDTO
     */
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        // get order by id
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());

        // only if orders in status " TO_BE_CONFIRMED" can mer-end reject order
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // pay status
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAID) {
            // user paid, request refund
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("申请退款：{}", refund);
        }

        // rejection requires refund, update order status by id ( reason/time of cancellation)
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * cancel order
     *
     * @param ordersCancelDTO
     */
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
        // get order by id
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());

        // pay status
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == 1) {
            // user have been paid, request refund
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("apply for refund：{}", refund);
        }

        // cancel order from admin end request refund，update order status by id (reason of cancellation, time of cancellation)
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * delivery of order
     *
     * @param id
     */
    public void delivery(Long id) {
        // get order by id
        Orders ordersDB = orderMapper.getById(id);

        // check whether order exists ( 3 )
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // update order status as DELIVERY_IN_PROGRESS
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderMapper.update(orders);
    }

    /**
     * complete order
     *
     * @param id
     */
    public void complete(Long id) {
        // get order by id
        Orders ordersDB = orderMapper.getById(id);

        // check whether order exists ( 4 )
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // update order status as COMPLETE
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * order reminder
     *
     * @param id
     */
    public void reminder(Long id) {
        // check whether order exists
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // accomplish reminder through WebSocket
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);// 2 represents reminder
        map.put("orderId", id);
        map.put("content", "order number：" + orders.getNumber());
        String msg = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(msg);
    }

}
