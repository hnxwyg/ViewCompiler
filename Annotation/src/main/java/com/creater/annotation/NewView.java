package com.creater.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by luwei on 18-6-7.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface NewView {
    int width() default -1;
    int height() default -1;
    int visible() default View.VISIBLE;
    int bg() default 0;
    boolean focusable() default false;
    String parent() default "";
    Margin margin() default @Margin;
    Padding padding() default @Padding;
    Listener listener() default @Listener;
}
