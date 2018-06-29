package com.creater.process;

import com.creater.annotation.NewView;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Element;

import static com.creater.process.ViewProcesser.TARGET_NAME;
import static com.creater.process.ViewProcesser.VIEW_NAME;
import static com.creater.process.ViewProcesser.isEmpty;

/**
 * Created by luwei on 18-6-26.
 */

public class ListenerProcesser implements IProcesser{
    @Override
    public CodeBlock generateCodeBlock(Element element) {
        NewView view = element.getAnnotation(NewView.class);
        String keyListener = view.listener().keyListener();
        String clickListener = view.listener().clickListener();
        String focusListenr = view.listener().focusListener();
        CodeBlock.Builder builder = CodeBlock.builder();
        if (!isEmpty(keyListener) || !isEmpty(clickListener) || !isEmpty(focusListenr)){
            if (!isEmpty(keyListener)){
                builder.addStatement(VIEW_NAME + ".setOnKeyListener(" + TARGET_NAME + ".$L)",keyListener);
            }
            if (!isEmpty(clickListener)){
                builder.addStatement(VIEW_NAME + ".setOnClickListener(" + TARGET_NAME + ".$L)",clickListener);
            }
            if (!isEmpty(focusListenr)){
                builder.addStatement(VIEW_NAME + ".setOnFocusChangeListener(" + TARGET_NAME + ".$L)",focusListenr);
            }
        }
        return builder.build();
    }
}
