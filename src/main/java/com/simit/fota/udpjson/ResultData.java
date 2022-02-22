/**
  * Copyright 2022 json.cn 
  */
package com.simit.fota.udpjson;

/**
 * Auto-generated: 2022-02-16 9:43:38
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class ResultData {

    private String DeviceIdKey;
    private ModuleKey ModuleKey;
    private UIMKey UIMKey;
    private RadioKey RadioKey;
    private NetworkInfoKey NetworkInfoKey;
    private SIMChangeResultKey SIMChangeResultKey;
    public void setDeviceIdKey(String DeviceIdKey) {
         this.DeviceIdKey = DeviceIdKey;
     }
     public String getDeviceIdKey() {
         return DeviceIdKey;
     }

    public void setModuleKey(ModuleKey ModuleKey) {
         this.ModuleKey = ModuleKey;
     }
     public ModuleKey getModuleKey() {
         return ModuleKey;
     }

    public void setUIMKey(UIMKey UIMKey) {
         this.UIMKey = UIMKey;
     }
     public UIMKey getUIMKey() {
         return UIMKey;
     }

    public void setRadioKey(RadioKey RadioKey) {
         this.RadioKey = RadioKey;
     }
     public RadioKey getRadioKey() {
         return RadioKey;
     }

    public void setNetworkInfoKey(NetworkInfoKey NetworkInfoKey) {
         this.NetworkInfoKey = NetworkInfoKey;
     }
     public NetworkInfoKey getNetworkInfoKey() {
         return NetworkInfoKey;
     }

    public void setSIMChangeResultKey(SIMChangeResultKey SIMChangeResultKey) {
         this.SIMChangeResultKey = SIMChangeResultKey;
     }
     public SIMChangeResultKey getSIMChangeResultKey() {
         return SIMChangeResultKey;
     }

    @Override
    public String toString() {
        return "ResultData{" +
                "DeviceIdKey='" + DeviceIdKey + '\'' +
                ", ModuleKey=" + ModuleKey +
                ", UIMKey=" + UIMKey +
                ", RadioKey=" + RadioKey +
                ", NetworkInfoKey=" + NetworkInfoKey +
                ", SIMChangeResultKey=" + SIMChangeResultKey +
                '}';
    }
}