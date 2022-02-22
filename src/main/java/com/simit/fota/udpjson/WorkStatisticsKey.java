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
public class WorkStatisticsKey {

    @Override
    public String toString() {
        return "WorkStatisticsKey{" +
                "WorkTimesKey=" + WorkTimesKey +
                ", WorkHoursKey=" + WorkHoursKey +
                '}';
    }

    private int WorkTimesKey;
    private int WorkHoursKey;
    public void setWorkTimesKey(int WorkTimesKey) {
         this.WorkTimesKey = WorkTimesKey;
     }
     public int getWorkTimesKey() {
         return WorkTimesKey;
     }

    public void setWorkHoursKey(int WorkHoursKey) {
         this.WorkHoursKey = WorkHoursKey;
     }
     public int getWorkHoursKey() {
         return WorkHoursKey;
     }

}