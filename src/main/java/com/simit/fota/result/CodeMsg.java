package com.simit.fota.result;

import lombok.Data;

@Data
public class CodeMsg {


    public static CodeMsg LOGOUT_SUCCESS = new CodeMsg(200,"logout success",false);
    public static CodeMsg PARAM_ERROR = new CodeMsg(5001,"param error");
    private int code;
    private String message;
    private boolean isError = true;

    //通用模块
    public static CodeMsg SUCCESS = new CodeMsg(200, "success",false);
    public static CodeMsg Sign_SUCCESS = new CodeMsg(201, "sign success",false);
    public static CodeMsg DEVICE_ADD_SUCCESS = new CodeMsg(201, "device add success",false);
    public static CodeMsg PROJECT_ADD_SUCCESS = new CodeMsg(201, "project create success",false);
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg NO_AUTHENTICATION = new CodeMsg(500102, "权限认证异常：%s");

    public static CodeMsg IP_NOT_ALLOW = new CodeMsg(500102, "ip已被拦截");


    //登录模块 5002xx
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或已失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "密码不能为空");
    public static CodeMsg USER_ERROR = new CodeMsg(500214, "username error");
    public static CodeMsg USER_NO_ACTIVATION = new CodeMsg(500214, "please activate first");

    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "password error");
    public static CodeMsg USERNAME_PASSWORD_ERROR = new CodeMsg(500231, "username or password error");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500222, "phone illegal");
    public static CodeMsg USER_EMPTY = new CodeMsg(500216, "username empty");
    public static CodeMsg USER_NOT_LOGIN = new CodeMsg(500217, "用户未登录");
    public static CodeMsg USERNAME_EXIST = new CodeMsg(500218, "username duplicate");
    public static CodeMsg USER_NOT_EXIST = new CodeMsg(500219, "user not exist");
    public static CodeMsg USER_INFO_ERROR = new CodeMsg(500226,"用户信息与登录的不一致");

    public static CodeMsg USER_ACCOUNT_EXPIRED = new CodeMsg(500219, "账号过期");
    public static CodeMsg USER_CREDENTIALS_EXPIRED = new CodeMsg(500220, "密码过期");
    public static CodeMsg USER_ACCOUNT_LOCKED = new CodeMsg(500221, "账号锁定");
    public static CodeMsg ACTIVATE_DUPLICATE = new CodeMsg(500223,"user has been activated");

    //下发命令模块 5003xx
    public static CodeMsg COMMAND_ERROR = new CodeMsg(500310, "命令下发失败");

    //上传Excel模块 5004XX
    public static CodeMsg NOT_ALLOWED_EMPTY_FILE = new CodeMsg(500401, "上传文件不能为空");
    public static CodeMsg FILE_FORMAT_IS_INCORRECT = new CodeMsg(500402, "上传文件格式不正确");
    public static CodeMsg UPLOAD_EXCEL_SUCCESS = new CodeMsg(500403, "文件上传成功",false);
    public static CodeMsg TITLE_NOT_MATCH_TEMPLATE = new CodeMsg(500404, "标题与模板不匹配");
    public static CodeMsg REQUEST_FAILED = new CodeMsg(500405, "请求失败，请您稍后再试");

    //设备模块
    public static CodeMsg IMEI_EMPTY  = new CodeMsg(500501,"IMEI empty");
    public static CodeMsg BRAND_NOT_EXIST  = new CodeMsg(500502,"brand not exist");
    public static CodeMsg TYPE_NOT_EXIST  = new CodeMsg(500503,"type not exist");
    public static CodeMsg IMEI_DUPLICATE = new CodeMsg(500503,"IMEI duplicate");
    public static CodeMsg DEVICE_NOT_EXIST = new CodeMsg(500504,"device not exist");

    //项目模块
    public static CodeMsg PROJECT_NOT_EXIST = new CodeMsg(500601,"project not exist");
    public static CodeMsg PROJECT_EMPTY = new CodeMsg(500602,"project empty");
    public static CodeMsg PROJECT_NAME_EMPTY = new CodeMsg(500603,"project name empty");
    public static CodeMsg PROJECT_DUPLICATE = new CodeMsg(500604,"project name duplicate");
    public static CodeMsg PROJECT_ID_EMPTY = new CodeMsg(500605,"project id empty");

    //版本模块
    public static CodeMsg SW_RLSE_NOT_EXIST = new CodeMsg(500701,"SWrlse not exist");
    public static CodeMsg INTIAL_VERSION_EMPTY = new CodeMsg(500702,"initial version empty");
    public static CodeMsg VERSION_DUPLICATE = new CodeMsg(500703,"version duplicate");



    public static CodeMsg TOKEN_INVALID = new CodeMsg(403,"token invalid");
    public static CodeMsg EXCEL_INVALID = new CodeMsg(500800,"上传文件不符合要求哦");


    public CodeMsg(int code, String msg) {
        this.code = code;
        this.message = msg;
    }
    public CodeMsg(int code, String msg,boolean isError) {
        this.code = code;
        this.message = msg;
        this.isError = isError;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }


    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.message, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + message + '\'' +
                '}';
    }
}
