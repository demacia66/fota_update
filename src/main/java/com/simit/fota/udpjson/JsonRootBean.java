/**
  * Copyright 2022 json.cn 
  */
package com.simit.fota.udpjson;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * Auto-generated: 2022-02-16 9:43:38
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class JsonRootBean {

    private String resultMessage;
    private String resultCode;
    private JSONArray resultData;
    private String responseData;
    private String DeviceIDKey;
    private String RandNumKey;
    private String PuidKey;
    private String CMDKey;
    private String ControlKey;


    @Override
    public String toString() {
        return "JsonRootBean{" +
                "resultMessage='" + resultMessage + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", resultData=" + resultData +
                ", responseData='" + responseData + '\'' +
                ", DeviceIDKey='" + DeviceIDKey + '\'' +
                ", RandNumKey='" + RandNumKey + '\'' +
                ", PuidKey='" + PuidKey + '\'' +
                ", CMDKey='" + CMDKey + '\'' +
                ", ControlKey='" + ControlKey + '\'' +
                '}';
    }

    public void setResultMessage(String resultMessage) {
         this.resultMessage = resultMessage;
     }
     public String getResultMessage() {
         return resultMessage;
     }

    public void setResultCode(String resultCode) {
         this.resultCode = resultCode;
     }
     public String getResultCode() {
         return resultCode;
     }

//    public void setResultData(List<ResultData> resultData) {
//         this.resultData = resultData;
//     }
//     public List<ResultData> getResultData() {
//         return resultData;
//     }


    public JSONArray getResultData() {
        return resultData;
    }

    public void setResultData(JSONArray resultData) {
        this.resultData = resultData;
    }

    public void setResponseData(String responseData) {
         this.responseData = responseData;
     }
     public String getResponseData() {
         return responseData;
     }

    public void setDeviceIDKey(String DeviceIDKey) {
         this.DeviceIDKey = DeviceIDKey;
     }
     public String getDeviceIDKey() {
         return DeviceIDKey;
     }

    public void setRandNumKey(String RandNumKey) {
         this.RandNumKey = RandNumKey;
     }
     public String getRandNumKey() {
         return RandNumKey;
     }

    public void setPuidKey(String PuidKey) {
         this.PuidKey = PuidKey;
     }
     public String getPuidKey() {
         return PuidKey;
     }

    public void setCMDKey(String CMDKey) {
         this.CMDKey = CMDKey;
     }
     public String getCMDKey() {
         return CMDKey;
     }

    public void setControlKey(String ControlKey) {
         this.ControlKey = ControlKey;
     }
     public String getControlKey() {
         return ControlKey;
     }

}