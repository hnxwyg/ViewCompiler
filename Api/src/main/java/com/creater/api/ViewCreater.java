package com.creater.api;

import android.content.Context;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ViewCreater {
    private static final String SUFFIX = "_NewView";
    private static final String METHOD_NAME = "doNewView";
    private static Map<String,Method> methodMap = new HashMap<>();
    public static void newView(Context ctx,Object target){
        String clazz = target.getClass().getName();
        String newClazz = clazz + SUFFIX;
        try {
            Method method = methodMap.get(newClazz);
            if (method == null){
                method = Class.forName(newClazz).getDeclaredMethod(METHOD_NAME,Context.class,target.getClass());
                methodMap.put(newClazz,method);
            }
            method.invoke(null,ctx,target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
