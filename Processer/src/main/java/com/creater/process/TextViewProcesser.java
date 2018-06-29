package com.creater.process;

import android.text.TextUtils;
import android.view.Gravity;

import com.creater.annotation.CompoundDrawable;
import com.creater.annotation.Text;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Element;

import static com.creater.process.ViewProcesser.VIEW_NAME;

/**
 * Created by luwei on 18-6-26.
 */

public class TextViewProcesser implements IProcesser{
    @Override
    public CodeBlock generateCodeBlock(Element element) {
        Text textView = element.getAnnotation(Text.class);
        CodeBlock.Builder builder = CodeBlock.builder();
        if (textView != null){
            if (!"".equals(textView.text())){
                builder.addStatement(VIEW_NAME + ".setText(\"$L\")",textView.text());
            }else if(textView.textId() != 0){
                builder.addStatement(VIEW_NAME + ".setText($L)",textView.textId());
            }
            if (textView.textColor() != -1){
                builder.addStatement(VIEW_NAME + ".setTextColor($L)",textView.textColor());
            }else if(textView.textColorId() != 0){
                builder.addStatement("int color = ctx.getResources().getColor($L)",textView.textColorId());
                builder.addStatement(VIEW_NAME + ".setTextColor(color)");
            }

            if (!"".equals(textView.hint())){
                builder.addStatement(VIEW_NAME + ".setHint(\"$L\")",textView.hint());
            }else if(textView.hintId() != 0){
                builder.addStatement(VIEW_NAME + ".setHint($L)",textView.hintId());
            }
            if (textView.hintColor() != -1){
                builder.addStatement(VIEW_NAME + ".setHintTextColor($L)",textView.hintColor());
            }else if(textView.hintColorId() != 0){
                builder.addStatement("int color = ctx.getResources().getColor($L)",textView.hintColorId());
                builder.addStatement(VIEW_NAME + ".setHintTextColor(color)");
            }

            if (textView.gravity() != (Gravity.TOP | Gravity.START))
                builder.addStatement(VIEW_NAME + ".setGravity($L)",textView.gravity());
            if (textView.textSize() != 0){
                builder.addStatement(VIEW_NAME + ".setTextSize($L)",textView.textSize());
            }
            if (textView.maxWidth() != 0){
                builder.addStatement(VIEW_NAME + ".setMaxWidth($L)",textView.maxWidth());
            }
            if (textView.maxHeight() != 0){
                builder.addStatement(VIEW_NAME + ".setMaxHeight($L)",textView.maxHeight());
            }
            if (textView.minWidth() != 0){
                builder.addStatement(VIEW_NAME + ".setMinWidth($L)",textView.minWidth());
            }
            if (textView.minHeight() != 0){
                builder.addStatement(VIEW_NAME + ".setMinHeight($L)",textView.minHeight());
            }
            if (textView.line() != 0){
                builder.addStatement(VIEW_NAME + ".setLines($L)",textView.line());
            }
            if (textView.marquee()){
                builder.addStatement(VIEW_NAME + ".setSingleLine(true)");
                builder.addStatement(VIEW_NAME + ".setMarqueeRepeatLimit(-1)");
                builder.addStatement(VIEW_NAME + ".setEllipsize($T.MARQUEE)",TextUtils.TruncateAt.class);
            }
            CompoundDrawable compoundDrawable = textView.compoundDrawable();
            if (compoundDrawable.left() != 0 || compoundDrawable.top() != 0
                    || compoundDrawable.right() != 0 || compoundDrawable.bottom() != 0){
                builder.addStatement(VIEW_NAME + ".setCompoundDrawablesWithIntrinsicBounds($L,$L,$L,$L)",
                        compoundDrawable.left(),compoundDrawable.top(),compoundDrawable.right(),compoundDrawable.bottom());
                if (compoundDrawable.padding() != 0){
                    builder.addStatement(VIEW_NAME + ".setCompoundDrawablePadding($L)",compoundDrawable.padding());
                }
            }

        }
        return builder.build();
    }
}
