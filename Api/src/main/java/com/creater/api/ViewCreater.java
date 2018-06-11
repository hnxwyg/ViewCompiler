package com.creater.api;

import android.content.Context;

import java.lang.reflect.Method;

public class ViewCreater {
    private static final String SUFFIX = "_NewView";
    private static final String METHOD_NAME = "doNewView";
    public static void newView(Context ctx,Object target){
        String clazz = target.getClass().getName();
        String newClazz = clazz + SUFFIX;
        try {
            Method method = Class.forName(newClazz).getDeclaredMethod(METHOD_NAME,Context.class,target.getClass());
            method.invoke(null,ctx,target);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
