package com.shimo.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * <p>
 * 模拟Spring 只是简单的模拟
 * </p>
 *
 * @author 世墨
 * @since 2022/7/25 9:37
 */
public class ShiMoApplicationContext {

    private  Class configClass;
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>(); //bean 一些属性
    private Map<String, Object> singletonObjects = new HashMap<>(); // 单列池
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public ShiMoApplicationContext(Class configClass) {
        this.configClass = configClass;

        //解析配置类
        scan(configClass);

        //初始话 单列池的bean
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if ("singleton".equals(beanDefinition.getScope()) && !beanDefinition.isLazy()){
                final Object bean = createBean(beanName,beanDefinition);
                singletonObjects.put(beanName,bean);
            }
        }




    }

    private Object createBean(String beanName, BeanDefinition beanDefinition){
        //拿到bean类型
        Class clazz = beanDefinition.getType();

        try {
            //反射创建bean
            Object o = clazz.newInstance();

            //中间需要做一些处理

            //Autowired 处理 利用反射拿到属性 打破封装，并从 beanDefinitionMap 获取bean 进行依赖注入 set属性
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)){
                    final Object bean = getBean(field.getName());
                    field.setAccessible(true);
                    field.set(o,bean);
                }
            }

            //通过 接口实现的方式 通过框架来帮你做某件事情 Aware 回调
            if (o instanceof BeanNameAware){
                ((BeanNameAware) o).setBeanName(beanName);
            }


            // BeanPostProcessor bean的后置处理器
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                o = beanPostProcessor.postProcessBeforeInitialization(o, beanName);
            }

            //初始化
            if (o instanceof  InitializingBean){
                ((InitializingBean) o).afterPropertiesSet();
            }

            // BeanPostProcessor bean的后置处理器
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                o = beanPostProcessor.postProcessAfterInitialization(o, beanName);
            }

            return o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void scan(Class configClass){

        //ComponentScan 注解 --> 扫描路径 --> 获取数据
        final ComponentScan componentScan =(ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        //扫描路径
        //容器启动的时候 起始就应该将所有的bean 创建好
        final String path = componentScan.value();
        System.out.println("get scan path to " + path);
        final String replace = path.replace(".", "/");
        System.out.println("replace path to " + replace);
        final ClassLoader classLoader = componentScan.getClass().getClassLoader();
        final URL resource = classLoader.getResource(replace);
        System.out.println("whole path to " + resource);
        final File file = new File(resource.getFile());

        final List<File> files = new ArrayList<>();

        //判断是否是目录
        //当前目录下所有的文件拿出来
        if (file.isDirectory()){
            for (File f : file.listFiles()) {
                if (f.isDirectory()){
                    for (File listFile : f.listFiles()) {
                        if (!listFile.isDirectory()){
                            files.add(listFile);
                        }
                    }
                }else {
                    files.add(f);
                }
            }
        }

        System.out.println("扫描 包下的文件：" + files);

        for (File f : files) {
            //获取绝对路径
            final String absolutePath = f.getAbsolutePath();
            System.out.println("打印绝对路径" + absolutePath);

            final String com = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"))
                    .replace("\\", ".");
            System.out.println("获取 类的全限定名：" + com);
            try {
                final Class<?> aClass = classLoader.loadClass(com);
                //判断注解是否存在
                if (aClass.isAnnotationPresent(Component.class)){

                    //特殊处理 备注：源码中不是用的这种 虽然 意思差不多 但是做的更全面
                    if (BeanPostProcessor.class.isAssignableFrom(aClass)){
                        final BeanPostProcessor beanPostProcessor = (BeanPostProcessor) aClass.newInstance();
                        beanPostProcessorList.add(beanPostProcessor);
                    }


                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setType(aClass);
                    beanDefinition.setLazy(aClass.isAnnotationPresent(Lazy.class));
                    if (aClass.isAnnotationPresent(Scope.class)){
                        beanDefinition.setScope(aClass.getAnnotation(Scope.class).value());
                    }else {
                        beanDefinition.setScope("singleton");
                    }
                    String beanName = aClass.getAnnotation(Component.class).value();
                    if (beanName.isEmpty()) {
                        beanName = Introspector.decapitalize(aClass.getSimpleName());
                    }
                    beanDefinitionMap.put(beanName, beanDefinition);
                }

            }catch (ClassNotFoundException e){
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getBean(String beanName){
        final BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (Objects.isNull(beanDefinition)){
            throw new NullPointerException();
        }
        if (Objects.equals("singleton",beanDefinition.getScope())){
            Object bean = singletonObjects.get(beanName);
            if (Objects.isNull(bean)){
                bean = createBean(beanName,beanDefinition);
                singletonObjects.put(beanName,bean);
            }
            return bean;
        }else {
            return createBean(beanName,beanDefinition);
        }
    }
}
