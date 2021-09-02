package com.simit.fota.dao;

import com.simit.fota.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    @Select("select * from Users where username = #{username}")
    User findUserByUsername(String username);

    @Insert("insert into Users(username,password,Phone_Number) values(#{username},#{password},#{phoneNum})")
    int insertUser(User user);

    @Update("update Users set password = #{password} where username = #{username}")
    int updateUser(User user);

}
