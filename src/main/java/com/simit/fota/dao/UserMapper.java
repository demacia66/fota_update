package com.simit.fota.dao;

import com.simit.fota.entity.User;
import com.simit.fota.result.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

    @Select("select * from Users where username = #{username} and enable != 2")
    User findUserByUsername(String username);

    @Select("select * from Users where user_id = #{userId} and enable != 2")
    User findUserByUserId(@Param("userId") Integer userId);

    @Insert("insert into Users(username,password,Phone_Number,enable) values(#{username},#{password},#{Phone_Number},#{enable})")
    int insertUser(User user);

    @Update("update Users set password = #{password} where username = #{username}")
    int updateUser(User user);

    @Update("update Users set enable = '1' where user_id = #{userId}")
    int activateUser(@Param("userId") Integer userId);

    @Select("select count(*) from Users where enable != '2'")
    int userCount();

    @Select("select user_id,username,Phone_Number,enable from Users where enable != '2' order by user_id desc limit #{startRow},#{pageSize}")
    List<User> findAllUsers(Page page);

    @Update("update Users set enable = '2' where user_id = #{userId}")
    int deleteUser(@Param("userId") Integer userId);
}
