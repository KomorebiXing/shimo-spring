package com.shimo.user.service;

import com.shimo.spring.BeanPostProcessor;
import com.shimo.spring.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author 世墨
 * @since 2022/7/25 15:46
 */
@Component
public class ShiMoBeanProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        //初始化前的后处理
        System.out.println("初始化前的后处理");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        //初始化后的后处理
        System.out.println("初始化后的后处理");
        return bean;
    }
}
