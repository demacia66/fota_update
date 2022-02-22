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
public class UIMKey {

    @Override
    public String toString() {
        return "UIMKey{" +
                "ICCIDKey=" + ICCIDKey +
                ", IMSIKey=" + IMSIKey +
                '}';
    }

    private int ICCIDKey;
    private int IMSIKey;
    public void setICCIDKey(int ICCIDKey) {
         this.ICCIDKey = ICCIDKey;
     }
     public int getICCIDKey() {
         return ICCIDKey;
     }

    public void setIMSIKey(int IMSIKey) {
         this.IMSIKey = IMSIKey;
     }
     public int getIMSIKey() {
         return IMSIKey;
     }

}