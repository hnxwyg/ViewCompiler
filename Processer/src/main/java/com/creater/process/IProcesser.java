package com.creater.process;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Element;

/**
 * Created by luwei on 18-6-26.
 */

public interface IProcesser {
    CodeBlock generateCodeBlock(Element element);
}


