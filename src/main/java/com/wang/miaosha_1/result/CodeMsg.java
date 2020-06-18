package com.wang.miaosha_1.result;

public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "成功");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常%s");
    //登录模块5002××
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500200,"密码为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500201, "手机号为空");
    public static CodeMsg MOBILE_TYPE_ERROR = new CodeMsg(500202, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIT = new CodeMsg(500203, "手机号码不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500204, "用户输入密码错误");
    //商品模块5003××

    //订单模块5004××

    //秒杀模块5005××
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "秒杀结束");
    public static CodeMsg MIAO_SHA_REPEATABLE = new CodeMsg(5005001, "不能重复秒杀");
    /**
     * 带参数返回
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object...args){
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    private CodeMsg(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
