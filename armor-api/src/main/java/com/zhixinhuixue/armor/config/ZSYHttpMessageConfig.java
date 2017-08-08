package com.zhixinhuixue.armor.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Tate on 2017/8/8.
 */
@Configuration
public class ZSYHttpMessageConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addAll((new WebMvcConfigurationSupport() {
            public List<HttpMessageConverter<?>> defaultMessageConverters() {
                return super.getMessageConverters();
            }
        }).defaultMessageConverters());

        converters.stream().map(converter->{
            if (converter instanceof MappingJackson2HttpMessageConverter){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ((MappingJackson2HttpMessageConverter) converter).getObjectMapper().setDateFormat(dateFormat);
                ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
                return converter;
            }
            if (converter instanceof StringHttpMessageConverter){
                ((StringHttpMessageConverter) converter).setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
            }
            return converter;
        }).collect(Collectors.toList());
    }


}
