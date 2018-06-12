package com.creater.annotation;

import android.widget.ImageView;

public @interface Image {
    int src() default 0;
    ImageView.ScaleType scaleType() default ImageView.ScaleType.FIT_XY;
}
