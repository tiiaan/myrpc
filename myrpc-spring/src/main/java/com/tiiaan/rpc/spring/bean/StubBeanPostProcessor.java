package com.tiiaan.rpc.spring.bean;

import com.tiiaan.rpc.MyRpcClient;
import com.tiiaan.rpc.spring.annotation.MyReference;
import com.tiiaan.rpc.common.entity.MyRpcService;
import com.tiiaan.rpc.netty.NettyRpcClient;
import com.tiiaan.rpc.proxy.MyRpcClientProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Component
public class StubBeanPostProcessor implements BeanPostProcessor {

    //private MyRpcClient myRpcClient = SingletonFactory.getInstance(NettyRpcClient.class);

    private MyRpcClient myRpcClient = new NettyRpcClient();


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
