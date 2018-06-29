package com.creater.process;

import android.widget.ImageView;

import com.creater.annotation.Image;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Element;

import static com.creater.process.ViewProcesser.VIEW_NAME;

/**
 * Created by luwei on 18-6-26.
 */

public class ImageViewProcesser implements IProcesser{
    @Override
    public CodeBlock generateCodeBlock(Element element) {
        Image imageView = element.getAnnotation(Image.class);
        CodeBlock.Builder builder = CodeBlock.builder();
        if (imageView != null){
            if (imageView.src() != 0){
                builder.addStatement(VIEW_NAME + ".setImageResource($L)",imageView.src());
            }
            builder.addStatement(VIEW_NAME + ".setScaleType($T.$L)", ImageView.ScaleType.class,imageView.scaleType());
        }
        return builder.build();
    }
}
