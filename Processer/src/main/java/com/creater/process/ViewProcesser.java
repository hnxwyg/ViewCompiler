package com.creater.process;

import android.content.Context;
import android.widget.FrameLayout;

import com.creater.annotation.NewView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by luwei on 18-6-7.
 */
@AutoService(Processor.class)
public class ViewProcesser extends AbstractProcessor {

    private Filer mFiler;
    private Messager mMessager;
    private Elements mElementUtils;
    private RoundEnvironment mRoundEnv = null;
    private Map<String, List<Element>> elementMap = new HashMap<>();
    private static boolean hasProcess = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(NewView.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (hasProcess)
            return true;
        hasProcess = true;
        this.mRoundEnv = roundEnvironment;
        Set<? extends Element> bindViewElements = roundEnvironment.getElementsAnnotatedWith(NewView.class);
        for (Element element : bindViewElements) {
            //获取完成类名
            if (!(element instanceof VariableElement))
                continue;
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            String enclosingName = enclosingElement.getQualifiedName().toString();
            if (elementMap.get(enclosingName) == null)
                elementMap.put(enclosingName, new ArrayList<Element>());
            elementMap.get(enclosingName).add(element);
        }
        Set<Map.Entry<String, List<Element>>> entries = elementMap.entrySet();
        for (Map.Entry<String, List<Element>> entry : entries) {
            String clazz = entry.getKey();
            String pkg = clazz.substring(0, clazz.lastIndexOf("."));
            String name = clazz.substring(clazz.lastIndexOf(".") + 1, clazz.length());
            List<Element> elementList = entry.getValue();
            ClassName target = ClassName.get((TypeElement) elementList.get(0).getEnclosingElement());
            TypeSpec.Builder clazzTypeBuilder = TypeSpec.classBuilder(name + "_NewView")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            List<String> methods = new ArrayList<>();
            for (Element element : elementList) {
                //因为BindView只作用于filed，所以这里可直接进行强转
                VariableElement bindViewElement = (VariableElement) element;
                //3.获取注解的成员变量名
                String fieldName = bindViewElement.getSimpleName().toString();
                //3.获取注解的成员变量类型
                String classType = bindViewElement.asType().toString();
                //4.获取注解元数据
                NewView bindView = element.getAnnotation(NewView.class);
                String methodName = "add" + fieldName;
                String targetName = "target";
                methods.add(methodName);
                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(void.class)
                        .addParameter(Context.class, "ctx")
                        .addParameter(target, targetName);
                String parent = bindView.parent();
                String viewName = "v";
                methodBuilder.addStatement("$T " + viewName + "= new $T(ctx)",ClassName.get(bindViewElement.asType()),
                        ClassName.get(bindViewElement.asType()));
                if (parent != null && parent != "") {
                    CodeBlock block = getLayoutParamsCodeBlock(parent, element);
                    methodBuilder.addCode(block);
                    methodBuilder.addStatement(targetName + ".$L.addView(" + viewName + ",params)", parent);
                }
                CodeBlock paddingBlock = getPaddingCodeBlock(viewName,element);
                methodBuilder.addCode(paddingBlock);
                CodeBlock listenerBlock = getListenerCodeBlock(targetName,viewName,element);
                methodBuilder.addCode(listenerBlock);
                methodBuilder.addStatement(targetName + ".$L = " + viewName, fieldName);
                clazzTypeBuilder.addMethod(methodBuilder.build());
            }
            JavaFile javaFile = JavaFile.builder(pkg, clazzTypeBuilder.build())
                    .build();
            try {
                javaFile.writeTo(mFiler);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void note(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void note(String format, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(format, args));
    }

    private CodeBlock getLayoutParamsCodeBlock(String field, Element element) {
        VariableElement variableElement = getFieldElement(field, element);
        NewView view = element.getAnnotation(NewView.class);
        if (variableElement != null) {
            if ("android.widget.FrameLayout".equals(variableElement.asType().toString())){
                return CodeBlock.builder().addStatement("$T params = new $T($L,$L)", FrameLayout.LayoutParams.class,
                        FrameLayout.LayoutParams.class,view.width(),view.height())
                        .addStatement("params.leftMargin = $L",view.margin().left())
                        .addStatement("params.topMargin = $L",view.margin().top())
                        .addStatement("params.rightMargin = $L",view.margin().right())
                        .addStatement("params.bottomMargin = $L",view.margin().bottom()).build();
            }
        }
        return null;
    }

    private CodeBlock getPaddingCodeBlock(String viewName,Element element){
        NewView view = element.getAnnotation(NewView.class);
        return CodeBlock.builder().addStatement(viewName + ".setPadding($L,$L,$L,$L)",
                view.padding().left(),
                view.padding().top(),
                view.padding().right(),
                view.padding().bottom()).build();
    }

    private CodeBlock getListenerCodeBlock(String target,String viewName,Element element){
        NewView view = element.getAnnotation(NewView.class);
        String keyListener = view.listener().keyListener();
        String clickListener = view.listener().clickListener();
        String focusListenr = view.listener().focusListener();
        if (!isEmpty(keyListener) || !isEmpty(clickListener) || !isEmpty(focusListenr)){
            CodeBlock.Builder builder = CodeBlock.builder();
            if (!isEmpty(keyListener)){
                builder.addStatement(viewName + ".setOnKeyListener(" + target + ".$L)",keyListener);
            }
            if (!isEmpty(clickListener)){
                builder.addStatement(viewName + ".setOnClickListener(" + target + ".$L)",clickListener);
            }
            if (!isEmpty(focusListenr)){
                builder.addStatement(viewName + ".setOnFocusChangeListener(" + target + ".$L)",focusListenr);
            }
            return builder.build();
        }
        return null;
    }

    public static boolean isEmpty(String s){
        if (s == null || s.equals(""))
            return true;
        return false;
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