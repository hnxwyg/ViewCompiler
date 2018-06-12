package com.creater.annotation;

public @interface Text {
    String text() default "";
    int textId() default 0;
    int textColor() default -1;
    int textColorId() default 0;
    int textSize() default -1;
}
