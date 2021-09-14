package com.simit.fota.service;

import com.simit.fota.dao.NetworkTypeMapper;
import com.simit.fota.entity.NetworkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetworkTypeService {

    @Autowired
    private NetworkTypeMapper typeMapper;

    public NetworkType findNetworkTypeByName(String Name) {
        return typeMapper.findNetworkTypeByName(Name);
    }
}
