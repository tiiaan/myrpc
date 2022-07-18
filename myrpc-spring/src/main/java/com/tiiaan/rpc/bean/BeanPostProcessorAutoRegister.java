package com.tiiaan.rpc.bean;

import com.tiiaan.rpc.annotation.MyService;
import com.tiiaan.rpc.entity.MyRpcService;
import com.tiiaan.rpc.factory.SingletonFactory;
import com.tiiaan.rpc.provider.ServiceProvider;
import com.tiiaan.rpc.provider.impl.ServiceProviderImpl;
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
public class BeanPostProcessorAutoRegister implements BeanPostProcessor {

    private ServiceProvider serviceProvider;

    public BeanPostProcessorAutoRegister() {
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
