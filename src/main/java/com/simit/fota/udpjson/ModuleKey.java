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
public class ModuleKey {

    @Override
    public String toString() {
        return "ModuleKey{" +
                "ModelKey='" + ModelKey + '\'' +
                ", SWVersionKey='" + SWVersionKey + '\'' +
                ", ProductVersionKey='" + ProductVersionKey + '\'' +
                ", IMEIKey=" + IMEIKey +
                '}';
    }

    private String ModelKey;
    private String SWVersionKey;
    private String ProductVersionKey;
    private long IMEIKey;
    public void setModelKey(String ModelKey) {

        this.ModelKey = ModelKey;
     }

     public String getModelKey() {
         return ModelKey;
     }

    public void setSWVersionKey(String SWVersionKey) {
         this.SWVersionKey = SWVersionKey;
     }
     public String getSWVersionKey() {
         return SWVersionKey;
     }

    public void setProductVersionKey(String ProductVersionKey) {
         this.ProductVersionKey = ProductVersionKey;
     }
     public String getProductVersionKey() {
         return ProductVersionKey;
     }

    public void setIMEIKey(long IMEIKey) {
         this.IMEIKey = IMEIKey;
     }

     public long getIMEIKey() {
         return IMEIKey;
     }

}