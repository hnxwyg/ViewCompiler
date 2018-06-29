package com.creater.annotation;

import android.widget.ImageView;

public @interface Image {
    int src() default 0;//需要显示的资源id
    ImageView.ScaleType scaleType() default ImageView.ScaleType.FIT_XY;//显示类型
}
