package com.simit.fota.result;

public class CodeMsg {
    private int code;
    private String msg;

    //通用模块
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg NO_AUTHENTICATION = new CodeMsg(500102, "权限认证异常：%s");

    public static CodeMsg IP_NOT_ALLOW = new CodeMsg(500102, "ip已被拦截");


    //登录模块 5002xx
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或已失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "密码不能为空");
    public static CodeMsg USER_ERROR = new CodeMsg(500214, "用户不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "用户名或密码错误");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500222, "手机号格式错误");
    public static CodeMsg USER_EMPTY = new CodeMsg(500216, "用户不能为空");
    public static CodeMsg USER_NOT_LOGIN = new CodeMsg(500217, "用户未登录");
    public static CodeMsg USERNAME_EXIST = new CodeMsg(500218, "用户名已存在");
    public static CodeMsg USER_INFO_ERROR = new CodeMsg(500226,"用户信息与登录的不一致");

    public static CodeMsg USER_ACCOUNT_EXPIRED = new CodeMsg(500219, "账号过期");
    public static CodeMsg USER_CREDENTIALS_EXPIRED = new CodeMsg(500220, "密码过期");
    public static CodeMsg USER_ACCOUNT_LOCKED = new CodeMsg(500221, "账号锁定");

    //下发命令模块 5003xx
    public static CodeMsg COMMAND_ERROR = new CodeMsg(500310, "命令下发失败");

    //上传Excel模块 5004XX
    public static CodeMsg NOT_ALLOWED_EMPTY_FILE = new CodeMsg(500401, "上传文件不能为空");
    public static CodeMsg FILE_FORMAT_IS_INCORRECT = new CodeMsg(500402, "上传文件格式不正确");
    public static CodeMsg UPLOAD_EXCEL_SUCCESS = new CodeMsg(500403, "文件上传成功");
    public static CodeMsg TITLE_NOT_MATCH_TEMPLATE = new CodeMsg(500404, "标题与模板不匹配");
    public static CodeMsg REQUEST_FAILED = new CodeMsg(500405, "请求失败，请您稍后再试");


    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
