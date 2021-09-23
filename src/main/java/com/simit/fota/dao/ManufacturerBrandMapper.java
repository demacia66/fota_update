package com.simit.fota.dao;

import com.simit.fota.entity.ManufacturerBrand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ManufacturerBrandMapper {
    @Select("select * from Manufacturer_Brand where ID = #{ID}")
    ManufacturerBrand findManufacturerBrandById(Integer ID);
    @Select("select * from Manufacturer_Brand where Manufacturer_Brand = #{brand}")
    ManufacturerBrand findManufacturerBrandByName(String brand);

    @Select("select Manufacturer_Brand from Manufacturer_Brand")
    List<String> findManufacturerBrands();
}
