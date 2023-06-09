package com.lizheng.takeout.config;

import com.lizheng.takeout.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @Author ZhengWen
 * @Date 2023/3/11 22:05
 * @Version 1.0
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("加载静态资源");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * 扩展mvc框架的消息转化器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("拓展消息转化器");
        //创建消息转化器对象
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        //设置对象转换器,底层使用jackson对象啊变成json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转化器对象追加到mvc框架的转换器容器集合中
        converters.add(0,messageConverter);

    }
}
