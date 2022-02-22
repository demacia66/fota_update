package com.simit.fota.exception;

import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


//统一异常处理
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e){

        if (e instanceof GlobalException){
            GlobalException ex = (GlobalException)e;
            return Result.error(ex.getCodeMsg());
        } else if (e instanceof BindException){
            BindException ex = (BindException)e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }
//        else if (e instanceof AuthenticationException){
//            AuthenticationException ex = (AuthenticationException)e;
//            String msg = ex.getMessage();
//            return Result.error(CodeMsg.NO_AUTHENTICATION.fillArgs(msg));
//        }else if (e instanceof AccessDeniedException){
//            AccessDeniedException ex = (AccessDeniedException)e;
//            String msg = ex.getMessage();
//            return Result.error(CodeMsg.NO_AUTHENTICATION.fillArgs(msg));
//        }
        else {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}

