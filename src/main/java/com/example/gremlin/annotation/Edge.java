package com.example.gremlin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sa
 * @date 9.02.2021
 * @time 12:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Edge
{
    /**
     * The label(gremlin reserved) of given Edge, can add Edge by label.
     * @return class name if not specify.
     */
    String label() default "";
}
