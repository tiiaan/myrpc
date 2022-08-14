
package com.tiiaan.rpc.annotation;

import org.springframework.stereotype.Service;

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
@Service
public @interface MyService {

    String version() default "";

}
