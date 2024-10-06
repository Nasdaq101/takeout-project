package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * order list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     * order status 1pending payment 2to be confirmed 3confirmed, to be delivered 4on_delivery 5completed 6cancelled
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer CONFIRMED = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED = 5;
    public static final Integer CANCELLED = 6;

    /**
     * payment status 0unpaid 1paid 2refund
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    private static final long serialVersionUID = 1L;

    private Long id;

    //order number
    private String number;

    //order status 1pending payment 2to be confirmed 3confirmed, to be delivered 4on_delivery 5completed 6cancelled 7refund
    private Integer status;

    //user id
    private Long userId;

    //address book id
    private Long addressBookId;

    //order time
    private LocalDateTime orderTime;

    //checkout time
    private LocalDateTime checkoutTime;

    //payment method 1wechat，2zhifubao
    private Integer payMethod;

    //payment status 0unpaid 1paid 2refund
    private Integer payStatus;

    //amount received
    private BigDecimal amount;

    //description
    private String remark;

    //user name
    private String userName;

    //phone
    private String phone;

    //address
    private String address;

    //consignee
    private String consignee;

    //reason of cancellation
    private String cancelReason;

    //reason of rejection
    private String rejectionReason;

    //order cancellation time
    private LocalDateTime cancelTime;

    //estimated delivery
    private LocalDateTime estimatedDeliveryTime;

    //delivery status  1now  0choose time
    private Integer deliveryStatus;

    //delivery time
    private LocalDateTime deliveryTime;

    //pack fee
    private int packAmount;

    //tableware number
    private int tablewareNumber;

    //tableware status  1based on dish amount  0 choose specific amount
    private Integer tablewareStatus;
}
