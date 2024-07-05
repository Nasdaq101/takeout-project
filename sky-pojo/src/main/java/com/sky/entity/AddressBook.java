package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * address book
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //user id
    private Long userId;

    //收货人
    private String consignee;

    //phone number
    private String phone;

    //sex 0-woman 1-man
    private String sex;

    //province code
    private String provinceCode;

    //province name
    private String provinceName;

    //city code
    private String cityCode;

    //city name
    private String cityName;

    //district code
    private String districtCode;

    //district name
    private String districtName;

    //detailed address
    private String detail;

    //label
    private String label;

    //default? 0-No 1-Yes
    private Integer isDefault;
}
