package com.creater.annotation;

/**
 * Created by luwei on 18-6-8.
 */

public @interface Listener {
    String keyListener() default "";
    String focusListener() default "";
    String clickListener() default "";
}
