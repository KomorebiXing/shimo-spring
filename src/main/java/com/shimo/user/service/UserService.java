package com.shimo.user.service;

import com.shimo.spring.Autowired;
import com.shimo.spring.BeanNameAware;
import com.shimo.spring.Component;
import com.shimo.spring.InitializingBean;
import java.util.StringJoiner;
import javax.swing.plaf.PanelUI;

/**
 * <p>
 *
 * </p>
 *
 * @author 世墨
 * @since 2022/7/25 9:52
 */
@Component
public class UserService implements BeanNameAware, InitializingBean {

    @Autowired
    private OrderService orderService;

    private String beanName;

    public void print(){
        System.out.println(orderService);
    }

    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("框架帮忙注入名字");
        this.beanName = beanName;
    }

    @Override
    //初始化
    public void afterPropertiesSet() {
        System.out.println("我要初始化做点事情,再容器创建对象的时候进行使用");
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserService.class.getSimpleName() + "[", "]")
                .add("orderService=" + orderService)
                .add("beanName='" + beanName + "'")
                .toString();
    }
}
