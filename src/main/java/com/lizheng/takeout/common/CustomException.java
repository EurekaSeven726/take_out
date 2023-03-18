package com.lizheng.takeout.common;

/**
 * @Author ZhengWen
 * @Date 2023/3/18 16:51
 * @Version 1.0
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}