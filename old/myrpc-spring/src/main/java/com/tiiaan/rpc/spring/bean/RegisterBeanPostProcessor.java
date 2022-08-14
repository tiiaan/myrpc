package com.tiiaan.rpc.spring.bean;

import com.tiiaan.rpc.spring.annotation.MyService;
import com.tiiaan.rpc.common.entity.MyRpcService;
import com.tiiaan.rpc.common.factory.SingletonFactory;
import com.tiiaan.rpc.holder.ServiceProvider;
import com.tiiaan.rpc.holder.impl.ServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Component
@Slf4j
public class RegisterBeanPostProcessor implements BeanPostProcessor {

    private ServiceProvider serviceProvider;

    public RegisterBeanPostProcessor() {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(MyService.class)) {
            MyService myService = bean.getClass().getAnnotation(MyService.class);
            MyRpcService myRpcService = MyRpcService.builder()
                    .service(bean)
                    .version(myService.version())
                    .build();
            serviceProvider.publishService(myRpcService);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
    
}
