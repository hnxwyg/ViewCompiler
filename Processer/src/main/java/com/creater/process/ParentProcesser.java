package com.creater.process;

import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.creater.annotation.NewView;
import com.squareup.javapoet.CodeBlock;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import static com.creater.process.ViewProcesser.TARGET_NAME;
import static com.creater.process.ViewProcesser.VIEW_NAME;

/**
 * Created by luwei on 18-6-26.
 */

public class ParentProcesser implements IProcesser{
    @Override
    public CodeBlock generateCodeBlock(Element element) {
        NewView view = element.getAnnotation(NewView.class);
        String parent = view.parent();
        if (parent != null && !parent.equals("")) {
            CodeBlock.Builder block = getLayoutParamsCodeBlock(parent, element);
            if (block != null){
                block.addStatement(TARGET_NAME + ".$L.addView(" + VIEW_NAME + ",params)", parent);
                return block.build();
            }
        }
        return CodeBlock.builder().build();
    }


    private CodeBlock.Builder getLayoutParamsCodeBlock(String field, Element element) {
        VariableElement variableElement = getFieldElement(field, element);
        NewView view = element.getAnnotation(NewView.class);
        Class paramsClazz = null;
        if (variableElement != null) {
            String parentType = variableElement.asType().toString();
            if ("android.widget.FrameLayout".equals(parentType)){
                paramsClazz = FrameLayout.LayoutParams.class;
            }else if("android.widget.LinearLayout".equals(parentType)){
                paramsClazz = LinearLayout.LayoutParams.class;
            }else if("android.widget.RelativeLayout".equals(parentType)){
                paramsClazz = RelativeLayout.LayoutParams.class;
            }else if("android.widget.AbsoluteLayout".equals(parentType)){
                paramsClazz = AbsoluteLayout.LayoutParams.class;
            }
        }
        if (paramsClazz != null)
            return getLayoutParamsCodeBlock(paramsClazz,view);
        return null;
    }

    private CodeBlock.Builder getLayoutParamsCodeBlock(Class clazz,NewView view){
        return CodeBlock.builder().addStatement("$T params = new $T($L,$L)", clazz,
                FrameLayout.LayoutParams.class,view.width(),view.height())
                .addStatement("params.leftMargin = $L",view.margin().left())
                .addStatement("params.topMargin = $L",view.margin().top())
                .addStatement("params.rightMargin = $L",view.margin().right())
                .addStatement("params.bottomMargin = $L",view.margin().bottom());
    }

    private VariableElement getFieldElement(String field, Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        List<? extends Element> elements = typeElement.getEnclosedElements();
        for (Element e : elements) {
            if (e instanceof VariableElement && e.getSimpleName().toString().equals(field))
                return (VariableElement) e;
        }
        return null;
    }
}
