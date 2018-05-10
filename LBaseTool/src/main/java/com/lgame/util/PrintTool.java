/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util;

import com.lgame.util.time.DateTimeTool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 将Exception转成String
 * @author leroy_boy
 */
public class PrintTool {
    private final static Map<String,Long> timeMap = new HashMap<>();

    public static void outTime(String flag,String str){
        flag = flag == null?"_":flag;
        final Long lastTime = timeMap.get(flag);
        final long curTime = System.currentTimeMillis();
        if(lastTime == null){
            System.out.println(DateTimeTool.getDateTime(new Date())+":"+str);
        }else {
            System.out.println(DateTimeTool.getDateTime(new Date())+":"+str+":"+(curTime-lastTime)+" ms");
        }
        timeMap.put(flag,curTime);
    }


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
        System.err.println(info);
    }

    /*public static void error(Object... info){
        System.out.println(info);
    }*/

    public static void error(String info,Exception e){
        e.printStackTrace();
        //System.out.println(info);
    }

    public static void error(Class info,Exception e){
        e.printStackTrace();
        //System.out.println(info);
    }
}
