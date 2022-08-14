package com.tiiaan.rpc.bean;

import com.tiiaan.rpc.annotation.MyService;
import com.tiiaan.rpc.common.entity.MyRpcService;
import com.tiiaan.rpc.holder.ServiceHolder;
import com.tiiaan.rpc.server.MyRpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */


//@Component
@Slf4j
public class MyRpcServerRegisterBeanPostProcessor implements BeanPostProcessor, CommandLineRunner {

    @Resource
    private ServiceHolder serviceHolder;
    @Resource
    private MyRpcServer nettyRpcServer;

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


    @Override
    public void run(String... args) throws Exception {
        nettyRpcServer.start();
    }

}
