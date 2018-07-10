package com.creater.process;

import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.creater.annotation.NewView;
import com.squareup.javapoet.CodeBlock;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.creater.process.ViewProcesser.TARGET_NAME;
import static com.creater.process.ViewProcesser.VIEW_NAME;

/**
 * Created by luwei on 18-6-26.
 */

public class ParentProcesser implements IProcesser{

    private static final Set<String>  VIEWGROUP_SET = new HashSet<>();
    static {
        VIEWGROUP_SET.add("android.widget.FrameLayout");
        VIEWGROUP_SET.add("android.widget.LinearLayout");
        VIEWGROUP_SET.add("android.widget.RelativeLayout");
        VIEWGROUP_SET.add("android.widget.AbsoluteLayout");
    }

    @Override
    public CodeBlock generateCodeBlock(Element element) {
        NewView view = element.getAnnotation(NewView.class);
        String parent = view.parent();
        if (parent != null && !parent.equals("")) {
            CodeBlock.Builder block = getLayoutParamsCodeBlock(parent, element);
            if (block != null){
                if (parent.equals("this")){
                    block.addStatement(TARGET_NAME + ".addView(" + VIEW_NAME + ",params)");
                }else{
                    block.addStatement(TARGET_NAME + ".$L.addView(" + VIEW_NAME + ",params)", parent);
                }
                return block.build();
            }
        }
        return CodeBlock.builder().build();
    }


    private CodeBlock.Builder getLayoutParamsCodeBlock(String field, Element element) {
        String parentType = null;
        if (field.equals("this")){
            parentType = getParentType(element);
            ViewProcesser.note("parenttype " + parentType);
        }else{
            VariableElement variableElement = getFieldElement(field, element);
            parentType = variableElement.asType().toString();
        }
        NewView view = element.getAnnotation(NewView.class);
        Class paramsClazz = null;
        int gravity = -1;
        if (parentType != null) {
            if ("android.widget.FrameLayout".equals(parentType)){
                paramsClazz = FrameLayout.LayoutParams.class;
                if (view.layoutGravity() != -1)
                    gravity = view.layoutGravity();
            }else if("android.widget.LinearLayout".equals(parentType)){
                paramsClazz = LinearLayout.LayoutParams.class;
            }else if("android.widget.RelativeLayout".equals(parentType)){
                paramsClazz = RelativeLayout.LayoutParams.class;
            }else if("android.widget.AbsoluteLayout".equals(parentType)){
                paramsClazz = AbsoluteLayout.LayoutParams.class;
            }
        }
        if (paramsClazz != null)
            return getLayoutParamsCodeBlock(paramsClazz,gravity,view);
        return null;
    }

    private CodeBlock.Builder getLayoutParamsCodeBlock(Class clazz,int gravity,NewView view){
        CodeBlock.Builder builder = CodeBlock.builder().addStatement("$T params = new $T($L,$L)", clazz,
                FrameLayout.LayoutParams.class,view.width(),view.height())
                .addStatement("params.leftMargin = $L",view.margin().left())
                .addStatement("params.topMargin = $L",view.margin().top())
                .addStatement("params.rightMargin = $L",view.margin().right())
                .addStatement("params.bottomMargin = $L",view.margin().bottom());
        if (gravity != -1)
            builder.addStatement("params.gravity = $L",gravity);
        return builder;
    }

    private String getParentType(Element element){
        String type = null;
        TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        while (typeElement != null){
            TypeMirror typeMirror = typeElement.getSuperclass();
            if (typeMirror != null){
                type = typeMirror.toString();
            }
            if (VIEWGROUP_SET.contains(type))
                return type;
            typeElement = (TypeElement) ViewProcesser.typeUtils.asElement(typeMirror);
        }
        return null;
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
