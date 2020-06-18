package com.wang.miaosha_1.exception;

import com.wang.miaosha_1.result.CodeMsg;

/**
 * 自定义全局异常类
 */

public class GlobalException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    //错误信息
    private CodeMsg cm;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
