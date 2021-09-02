package com.simit.fota.result;



import lombok.Data;

/**
 * @Author XuCheng
 * @Date 2020/4/26 15:54
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private String action;
    private T data;

    public Result(){
    }

    public Result(T data){
        this.code = 0;
        this.message = "success";
        this.data = data;
    }

    public Result(T data,String action){
        this.code = 0;
        this.message = "success";
        this.data = data;
        this.action = action;
    }

    private Result(CodeMsg codeMsg) {
        if (codeMsg == null){
            return;
        }
        this.code = codeMsg.getCode();
        this.message = codeMsg.getMsg();
    }

    private Result(CodeMsg codeMsg,String action) {
        if (codeMsg == null){
            return;
        }
        this.code = codeMsg.getCode();
        this.message = codeMsg.getMsg();
        this.action = action;
    }

    /**
     * 成功得时候调用
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data,String action){
        return new Result<T>(data,action);
    }


    /**
     * 失败得时候调用
     * @param codeMsg
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMsg codeMsg,String action){
        return new Result<T>(codeMsg,action);
    }


    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }

}
