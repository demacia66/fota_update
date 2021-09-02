package com.simit.fota.exception;

import com.simit.fota.result.CodeMsg;
import lombok.Data;

@Data
public class GlobalException extends RuntimeException{
    public static final long serialVersionUID = 1L;

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg cm){
        codeMsg = cm;
    }
}
