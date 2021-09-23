package com.simit.fota.dao;

import com.simit.fota.entity.NetworkType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NetworkTypeMapper {

    @Select("select * from Network_Type where Name = #{Name}")
    NetworkType findNetworkTypeByName(String Name);

    @Select("select Name from Network_Type")
    List<String> findNetworkTypes();
}
