package com.dd.ai_smart_course.utils;

public class BaseContext {

    public static ThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();

    public static void setCurrentId(int id) {
        threadLocal.set(id);
    }

    public static int getCurrentId() {
        Integer value = threadLocal.get();
        return value != null ? value : -1;
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
