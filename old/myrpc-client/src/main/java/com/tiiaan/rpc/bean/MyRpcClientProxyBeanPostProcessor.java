package com.tiiaan.rpc.bean;

import com.tiiaan.rpc.annotation.MyReference;
import com.tiiaan.rpc.client.MyRpcClient;
import com.tiiaan.rpc.common.entity.MyRpcService;
import com.tiiaan.rpc.proxy.MyRpcClientProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

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
