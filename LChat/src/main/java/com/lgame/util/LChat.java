package com.lgame.util;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/19.
 */
public class LChat {

    public static String getException(Exception e) {
        StringBuilder bs = new StringBuilder();
        StackTraceElement[] a = e.getStackTrace();
        bs.append("\n Message: ").append(e.fillInStackTrace()).append("");
        for (int i = 0; i < a.length; i++) {
            bs.append("\n ").append(a[i].getClassName()).append("(Line:").append(a[i].getLineNumber()).append(",Method:").append(a[i].getMethodName()).append(")");
        }
        return bs.toString();
    }

    public static void log(String info){
        System.out.println(info);
    }

    public static void info(String... info){
        System.out.println(info);
    }

    public static void error(String info){
        System.out.println(info);
    }

    public static void error(Object... info){
        System.out.println(info);
    }

    public static void error(String info,Exception e){
        System.out.println(info+":"+getException(e));
    }
}
