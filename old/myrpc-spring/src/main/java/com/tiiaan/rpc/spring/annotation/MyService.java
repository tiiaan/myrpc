
package com.tiiaan.rpc.spring.annotation;

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
public @interface MyService {

    String version() default "";

}
