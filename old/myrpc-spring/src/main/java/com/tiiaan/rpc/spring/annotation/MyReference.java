package com.tiiaan.rpc.spring.annotation;

import java.lang.annotation.*;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MyReference {

    String version() default "";

}
