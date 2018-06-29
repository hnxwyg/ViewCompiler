package com.creater.annotation;

/**
 * Created by luwei on 18-6-8.
 */

public @interface Listener {
    String keyListener() default "";//监听键值
    String focusListener() default "";//监听焦点变化
    String clickListener() default "";//监听点击事件
}
