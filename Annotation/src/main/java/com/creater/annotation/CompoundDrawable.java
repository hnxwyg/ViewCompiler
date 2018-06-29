package com.creater.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by luwei on 18-6-26.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface CompoundDrawable {
    int left() default 0;//左
    int top() default 0;//上
    int right() default 0;//右
    int bottom() default 0;//下
    int padding() default 0;//间距
}
