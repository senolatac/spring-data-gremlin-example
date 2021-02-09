package com.example.gremlin.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author sa
 * @date 8.02.2021
 * @time 16:57
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Property
{
}
