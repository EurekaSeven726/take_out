package com.lizheng.takeout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author ZhengWen
 * @Date 2023/3/11 21:50
 * @Version 1.0
 */
@Slf4j
//slf4j查看日志方便
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
//扫描filter过滤器用
public class TakeOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(TakeOutApplication.class,args);
        log.info("Take Out启动成功");
        //启动后控制台输出Take Out启动成功
    }
}
