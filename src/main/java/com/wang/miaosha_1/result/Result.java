package com.wang.miaosha_1.result;

public class Result<T> {
    private int code;
    private String msg;
    private T date;

    /**
     * 成功时候的调用
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    /**
     * 失败的时候的调用
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }

    private Result(T date) {
        this.code = 0;
        this.msg = "成功";
        this.date = date;
    }

    private Result(CodeMsg cm){
        if(cm == null){
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getDate() {
        return date;
    }
}
