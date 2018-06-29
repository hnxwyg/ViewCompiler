package com.creater.annotation;

import android.view.View;
import android.view.ViewGroup;

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
    int width() default ViewGroup.LayoutParams.WRAP_CONTENT;//宽
    int height() default ViewGroup.LayoutParams.WRAP_CONTENT;;//高
    int visible() default View.VISIBLE;//是否可显示,默认为可显示
    int bgId() default 0;//背景资源id
    int bgcolor() default -1;//背景颜色值
    int bgcolorId() default 0;//背景颜色id
    float alpha() default 0f;//透明度
    boolean focusable() default false;//是否可获取焦点
    boolean clip() default true;//是否clip
    boolean clickable() default false;//是否clickable
    boolean enable() default false;//是否enable
    boolean select() default false;//是否select
    String parent() default "";//父ViewGroup名称
    Margin margin() default @Margin;//Margin值注解
    Padding padding() default @Padding;//Padding值注解
    Listener listener() default @Listener;//监听注解
}
