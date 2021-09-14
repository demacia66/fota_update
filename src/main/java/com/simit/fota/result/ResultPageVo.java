package com.simit.fota.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//分页的result
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultPageVo<T>{

    private String state;
    private int code;
    private String message;
    private String action;
    private T data;

    //总页数
    private int totalCount;

    //当前页数
    private int currentPage;

    //每页条数
    private int pageSize;

    public ResultPageVo(T data){
        this.code = 200;
        this.message = "success";
        this.data = data;
        this.state = "ok";
    }
    public ResultPageVo(T data,Page page){
        this.code = 200;
        this.message = "success";
        this.data = data;
        this.state = "ok";
    }

    //本来想继承result，但是返现set不是静态方法，遂放弃
    public static <T> ResultPageVo<T> success(Page page,T data,String action){
        return null;
    }

}
