package com.simit.fota.service;

import com.simit.fota.dao.ManufacturerBrandMapper;
import com.simit.fota.entity.ManufacturerBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

    @Autowired
    private ManufacturerBrandMapper brandMapper;

    public ManufacturerBrand findManufacturerBrandByName(String brand) {
        return brandMapper.findManufacturerBrandByName(brand);
    }
}
