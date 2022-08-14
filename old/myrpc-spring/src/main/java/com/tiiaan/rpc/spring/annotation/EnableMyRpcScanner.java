package com.tiiaan.rpc.spring.annotation;

import com.tiiaan.rpc.spring.scanner.MyRpcServiceScanner;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(MyRpcServiceScanner.class)
public @interface EnableMyRpcScanner {

    String[] basePackage();

}
