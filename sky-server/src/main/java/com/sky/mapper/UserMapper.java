package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * search user by openid
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    /**
     * insert data
     * @param user
     */
    void insert(User user);

    /**
     * search user by id
     *
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    public User getById(Long userId);

    /**
     * dynamically count user amount
     *
     * @param map
     * @return
     */
    Integer countByMap(Map<String, Object> map);
}
