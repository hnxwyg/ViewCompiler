package com.creater.annotation;

import android.view.Gravity;

public @interface Text {
    String text() default "";//需要显示的字符串
    int textId() default 0;//需要显示的字符串id
    int textColor() default -1;//文字颜色值
    int textColorId() default 0;//文字颜色id
    int textSize() default 0;//文字大小
    int maxHeight() default 0;//最大高度
    int maxWidth() default 0;//最大宽度
    int minWidth() default 0;//最小宽度
    int minHeight() default 0;//最小高度
    int line() default 0;//行数
    String hint() default "";//默认文字
    int hintId() default 0;//默认文字id
    int hintColor() default -1;//默认文字颜色值
    int hintColorId() default 0;//默认文字颜色id
    boolean marquee() default false;//是否支持滚动
    int gravity() default Gravity.TOP | Gravity.START;//设置gravity
    CompoundDrawable compoundDrawable() default @CompoundDrawable;//文字四周图标
}
