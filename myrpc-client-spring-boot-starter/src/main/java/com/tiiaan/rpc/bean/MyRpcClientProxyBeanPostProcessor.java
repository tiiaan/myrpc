package com.tiiaan.rpc.bean;

import com.tiiaan.rpc.annotation.MyReference;
import com.tiiaan.rpc.client.MyRpcClient;
import com.tiiaan.rpc.common.entity.MyRpcService;
import com.tiiaan.rpc.proxy.MyRpcClientProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */


public class MyRpcClientProxyBeanPostProcessor implements BeanPostProcessor {

    @Resource
    private MyRpcClient myRpcClient;


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            MyReference myReference = field.getAnnotation(MyReference.class);
            if (myReference != null) {
                MyRpcService myRpcService = MyRpcService.builder()
                        .version(myReference.version())
                        .build();
                MyRpcClientProxy myRpcClientProxy = new MyRpcClientProxy(myRpcClient, myRpcService);
                Object proxy = myRpcClientProxy.getProxyInstance(field.getType());
                field.setAccessible(true);
                try {
                    field.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

}



//public class MyRpcClientProxyBeanPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {
//
//    private ApplicationContext applicationContext;
//    @Resource
//    private MyRpcClient myRpcClient;
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
//        for (String beanDefinitionName : beanDefinitionNames) {
//            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
//            String beanClassName = beanDefinition.getBeanClassName();
//            if (beanClassName != null) {
//                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.getClass().getClassLoader());
//                ReflectionUtils.doWithFields(clazz, field -> {
//                    MyReference myReference = AnnotationUtils.getAnnotation(field, MyReference.class);
//                    if (myReference != null) {
//                        Object bean = applicationContext.getBean(clazz);
//                        MyRpcService myRpcService = MyRpcService.builder()
//                                .version(myReference.version())
//                                .build();
//                        MyRpcClientProxy myRpcClientProxy = new MyRpcClientProxy(myRpcClient, myRpcService);
//                        Object proxy = myRpcClientProxy.getProxyInstance(field.getType());
//                        field.setAccessible(true);
//                        ReflectionUtils.setField(field, bean, proxy);
//                    }
//                });
//            }
//        }
//    }
//
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//
//}
