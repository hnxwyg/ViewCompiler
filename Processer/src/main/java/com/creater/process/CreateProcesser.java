package com.creater.process;

import android.view.View;

import com.creater.annotation.NewView;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

import static com.creater.process.ViewProcesser.VIEW_NAME;

/**
 * Created by luwei on 18-6-26.
 */

public class CreateProcesser implements IProcesser{
    @Override
    public CodeBlock generateCodeBlock(Element element) {
        //因为BindView只作用于filed，所以这里可直接进行强转
        VariableElement bindViewElement = (VariableElement) element;
        NewView view = element.getAnnotation(NewView.class);

        CodeBlock.Builder builder = CodeBlock.builder();
        builder.addStatement("$T " + VIEW_NAME + "= new $T(ctx)", ClassName.get(bindViewElement.asType()),
                ClassName.get(bindViewElement.asType()));
        if (view.visible() != View.VISIBLE)
            builder.addStatement(VIEW_NAME + ".setVisibility($L)",view.visible());
        if (view.bgcolor() != -1)
            builder.addStatement(VIEW_NAME + ".setBackgroundColor($L)",view.bgcolor());
        if (view.bgId() != 0)
            builder.addStatement(VIEW_NAME + ".setBackgroundResource($L)",view.bgId());
        if (view.focusable()){
            builder.addStatement(VIEW_NAME + ".setFocusable($L)",view.focusable());
            builder.addStatement(VIEW_NAME + ".setFocusableInTouchMode($L)",view.focusable());
        }
        if (view.bgcolorId() != 0){
            builder.addStatement("int color = ctx.getResources().getColor($L)",view.bgcolorId());
            builder.addStatement(VIEW_NAME + ".setBackgroundColor(color)");
        }
        if(!view.clip()){
            builder.addStatement(VIEW_NAME + ".setClipChildren($L)",view.clip());
            builder.addStatement(VIEW_NAME + ".setClipToPadding($L)",view.clip());
        }
        if (view.clickable()){
            builder.addStatement(VIEW_NAME + ".setClickable($L)",view.clickable());
        }
        if (view.enable()){
            builder.addStatement(VIEW_NAME + ".setEnabled($L)",view.enable());
        }
        if (view.select()){
            builder.addStatement(VIEW_NAME + ".setSelected($L)",view.select());
        }
        if (view.padding().left() != 0 || view.padding().left() != 0
                || view.padding().left() != 0 ||view.padding().left() != 0){
            builder.addStatement(VIEW_NAME + ".setPadding($L,$L,$L,$L)",
                    view.padding().left(),
                    view.padding().top(),
                    view.padding().right(),
                    view.padding().bottom());
        }
        return builder.build();
    }
}
