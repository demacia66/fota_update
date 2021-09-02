package com.simit.fota.dao;

import com.simit.fota.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    User findUserByUsername(String username);
}
