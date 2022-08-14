package com.tiiaan.rpc.bean;

import com.tiiaan.rpc.annotation.MyService;
import com.tiiaan.rpc.common.entity.MyRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */


//@Component
@Slf4j
public class RegisterBeanPostProcessor implements BeanPostProcessor {

    private com.tiiaan.rpc.holder.ServiceHolder serviceHolder;

    //public RegisterBeanPostProcessor() {
    //    serviceHolder = SingletonFactory.getInstance(ServiceHolder.class);
    //}

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(MyService.class)) {
            MyService myService = bean.getClass().getAnnotation(MyService.class);
            MyRpcService myRpcService = MyRpcService.builder()
                    .service(bean)
                    .version(myService.version())
                    .build();
            serviceHolder.setService(myRpcService);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
    
}
