package com.lizheng.test;

import org.junit.jupiter.api.Test;

/**
 * @Author ZhengWen
 * @Date 2023/3/18 22:22
 * @Version 1.0
 */
public class UploadFileTest {
    @Test
    public void test1() {
        String fileName = "test.jpg";
        String suffix= fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);

    }
}